package fr.spc.leosoliveres.chaldeas.model.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import fr.spc.leosoliveres.chaldeas.model.Site

@Dao
interface SiteDao {
	@Insert(onConflict = OnConflictStrategy.REPLACE)
	fun insertSite(s: Site)

	@Insert
	fun insertAll(l:List<Site>)

	@Query("SELECT * FROM sites ORDER BY site_id ASC")
	fun getAll(): LiveData<List<Site>>

	@Query("DELETE FROM sites")
	fun deleteAll()
}