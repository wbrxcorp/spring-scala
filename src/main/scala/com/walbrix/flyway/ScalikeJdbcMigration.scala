package com.walbrix.flyway

import java.sql.Connection

import org.flywaydb.core.api.migration.jdbc.JdbcMigration
import scalikejdbc.DBConnectionAttributes

/**
 * Created by shimarin on 14/11/16.
 */

class DBSession(con:Connection) extends scalikejdbc.DBSession {
  override val connectionAttributes = DBConnectionAttributes()
  override val isReadOnly: Boolean = false
  override val conn:Connection = con
}

trait ScalikeJdbcMigration extends JdbcMigration with scalikejdbc.SQLInterpolation {
  override def migrate(con:Connection):Unit = {
    migrate(new DBSession(con))
  }
  def migrate(implicit session:DBSession)
}
