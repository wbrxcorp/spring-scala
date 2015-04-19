package db.migration

import java.text.Normalizer

/**
 * Created by shimarin on 15/02/15.
 */
class V9999__TestData extends com.walbrix.flyway.ScalikeJdbcMigration with com.typesafe.scalalogging.slf4j.LazyLogging {

  // NFKC正規化
  private def normalize(str:String):String = Normalizer.normalize(str, Normalizer.Form.NFKC)

  // Loanパターン
  type Closable = { def close():Unit }
  def using[A <: Closable,B]( resource:A )( f:A => B ) = try(f(resource)) finally(resource.close)

  // マイグレーション実行
  override def migrate(implicit session: scalikejdbc.DBSession): Unit = {
    using(Option(this.getClass.getResourceAsStream("/13TOKYO.CSV.xz")).getOrElse(throw new java.io.FileNotFoundException)) { kenAll =>
      logger.debug("begin loading testdata")
      using (new org.tukaani.xz.XZInputStream(kenAll)) { xz =>
        using (new java.io.InputStreamReader(xz, "MS932")) { isr =>
          using (new au.com.bytecode.opencsv.CSVReader(isr, ',', '"')) { reader =>
            var line = reader.readNext()
            while (line != null) {
              val (jis_code, zip_code, city_kana, street_kana, pref, city, street) =
                (line(0), line(2),
                  normalize(line(4)),
                  normalize(line(5)),
                  line(6), line(7),
                  normalize(line(8)))
              sql"""insert into zip_code(jis_code,zip_code,city_kana,street_kana,pref,city,street)
              values(${jis_code},${zip_code},${city_kana},${street_kana},${pref},${city},${street})""".update().apply()
              line = reader.readNext()
            }
          }
        }
      }
      logger.debug("end loading testdata")
    }
  }
}
