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
  override def migrate(implicit session: DBSession): Unit = {
    Option(this.getClass.getResourceAsStream("/13TOKYO.CSV.xz")).foreach { kenAll =>
      try {
        val xz = new XZInputStream(kenAll)
        val isr = new InputStreamReader(xz, "MS932")
        val reader = new CSVReader(isr, ',', '"')
        var line = reader.readNext()
        while (line != null) {
          val jis_code = line(0)
          val zip_code = line(2)
          val city_kana = Normalizer.normalize(line(4), Normalizer.Form.NFKC)
          val street_kana = Normalizer.normalize(line(5), Normalizer.Form.NFKC)
          val pref = line(6)
          val city = line(7)
          val street = line(8)
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
