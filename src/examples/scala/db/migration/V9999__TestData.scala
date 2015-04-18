package db.migration

import java.text.Normalizer

/**
 * Created by shimarin on 15/02/15.
 */
class V9999__TestData extends com.walbrix.flyway.ScalikeJdbcMigration with com.typesafe.scalalogging.slf4j.LazyLogging {

  // NFKC正規化
  private def normalize(str:String):String = Normalizer.normalize(str, Normalizer.Form.NFKC)

  // マイグレーション実行
  override def migrate(implicit session: scalikejdbc.DBSession): Unit = {
    Option(this.getClass.getResourceAsStream("/13TOKYO.CSV.xz")).foreach { kenAll =>
      logger.debug("begin loading testdata")
      try {
        val xz = new org.tukaani.xz.XZInputStream(kenAll)
        val isr = new java.io.InputStreamReader(xz, "MS932")
        val reader = new au.com.bytecode.opencsv.CSVReader(isr, ',', '"')
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
        logger.debug("end loading testdata")
        isr.close()
        xz.close()
      }
      finally {
        kenAll.close()
      }
    }
  }
}
