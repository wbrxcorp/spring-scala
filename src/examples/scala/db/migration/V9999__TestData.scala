package db.migration

import java.io.InputStreamReader
import java.text.Normalizer

import au.com.bytecode.opencsv.CSVReader
import com.walbrix.flyway.{DBSession, ScalikeJdbcMigration}
import org.tukaani.xz.XZInputStream

/**
 * Created by shimarin on 15/02/15.
 */
class V9999__TestData extends ScalikeJdbcMigration {
  private def normalize(str:String):String = Normalizer.normalize(str, Normalizer.Form.NFKC)

  override def migrate(implicit session: DBSession): Unit = {
    Option(this.getClass.getResourceAsStream("/13TOKYO.CSV.xz")).foreach { kenAll =>
      try {
        val xz = new XZInputStream(kenAll)
        val isr = new InputStreamReader(xz, "MS932")
        val reader = new CSVReader(isr, ',', '"')
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
        isr.close()
        xz.close()
      }
      finally {
        kenAll.close()
      }
    }
  }
}
