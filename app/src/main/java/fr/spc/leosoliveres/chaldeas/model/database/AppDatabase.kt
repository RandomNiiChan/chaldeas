package fr.spc.leosoliveres.chaldeas.model.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import fr.spc.leosoliveres.chaldeas.model.Record
import fr.spc.leosoliveres.chaldeas.model.Report
import fr.spc.leosoliveres.chaldeas.model.Site
import fr.spc.leosoliveres.chaldeas.model.dao.RecordDao
import fr.spc.leosoliveres.chaldeas.model.dao.ReportDao
import fr.spc.leosoliveres.chaldeas.model.dao.SiteDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = [Site::class, Report::class, Record::class], version=1, exportSchema = true)
@TypeConverters(Converters::class)
abstract class AppDatabase: RoomDatabase() {
	abstract fun siteDao(): SiteDao
	abstract fun reportDao(): ReportDao
	abstract fun recordDao(): RecordDao


	private class AppDatabaseCallback(val context: Context) : RoomDatabase.Callback() {
		override fun onCreate(db: SupportSQLiteDatabase) {
			super.onCreate(db)
			ioThread() {
				val siteDao = getDatabase(context)!!.siteDao()
				val count = 20
				for (i in 0..count) {
					siteDao.insertSite(
						Site(
							"Station $i",
							((-90..89).random() + Math.random()).toFloat(),
							((-180..179).random() + Math.random()).toFloat()
						)
					)
				}
			}
		}
	}

	companion object {
		@Volatile
		private var INSTANCE: AppDatabase? = null

		fun getDatabase(context: Context): AppDatabase? {
			return INSTANCE ?: synchronized(this) {
				val instance = Room.databaseBuilder(
					context.applicationContext,
					AppDatabase::class.java,
					"app_database.sqlite"
				)
					.addCallback(AppDatabaseCallback(context))
					.build()
				INSTANCE = instance
				// return instance
				instance
			}
		}

	}
}