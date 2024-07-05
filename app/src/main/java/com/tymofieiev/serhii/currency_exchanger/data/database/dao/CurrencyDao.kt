package com.tymofieiev.serhii.currency_exchanger.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tymofieiev.serhii.currency_exchanger.data.database.entities.CurrencyBalanceEntity
import com.tymofieiev.serhii.currency_exchanger.data.database.entities.ExchangeOperationsEntity
import com.tymofieiev.serhii.currency_exchanger.data.database.entities.RateEntity
import kotlinx.coroutines.flow.Flow
import java.math.BigDecimal


/*
* Created by Serhii Tymofieiev
*/
@Dao
interface CurrencyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCurrency(data: CurrencyBalanceEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateRates(data: List<RateEntity>)

    @Query("SELECT * FROM currency_balance")
    fun getBalanceList(): Flow<List<CurrencyBalanceEntity>>

    @Query("SELECT * FROM rates WHERE pair_symbol =:pairSymbols")
    fun getRateByPairSymbol(pairSymbols: String): Flow<RateEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExcOperation(data: ExchangeOperationsEntity)

    @Query("SELECT * FROM exchange_operations")
    fun getExcOperationList(): Flow<List<ExchangeOperationsEntity>>

    @Query("UPDATE currency_balance SET  balance =:balance WHERE currency_symbol =:currencySymbol")
    fun updateBalance(currencySymbol: String, balance: BigDecimal)

    @Query("SELECT COUNT(id) FROM exchange_operations WHERE currency_sell_symbol =:currencySymbol")
    fun operationCountBySymbol(currencySymbol: String): Flow<Int>
    @Query("SELECT symbol FROM rates")
    fun getAllCurrencySymbolList(): Flow<List<String>>
    @Query("SELECT COUNT(currency_symbol) FROM currency_balance WHERE currency_symbol =:currencySymbol")
    fun isBalanceExist(currencySymbol: String): Int
}