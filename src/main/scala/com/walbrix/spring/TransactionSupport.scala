package com.walbrix.spring

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.transaction.{TransactionDefinition, PlatformTransactionManager}
import org.springframework.transaction.support.DefaultTransactionDefinition

/**
 * Created by shimarin on 15/10/14.
 */
trait TransactionSupport {
  @Autowired private var transactionManager:PlatformTransactionManager = _

  def tx[T](definition:TransactionDefinition)(f: =>T):T = {
    val status = transactionManager.getTransaction(definition)

    try {
      val rst = f
      transactionManager.commit(status)
      rst
    }
    finally {
      if (!status.isCompleted) transactionManager.rollback(status)
    }
  }

  def tx[T](f: =>T):T = {
    tx(new DefaultTransactionDefinition)(f)
  }

}
