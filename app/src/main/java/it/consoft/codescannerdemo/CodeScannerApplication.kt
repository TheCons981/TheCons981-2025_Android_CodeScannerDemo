package it.consoft.codescannerdemo

import android.app.Application
import androidx.room.Room
import it.consoft.codescannerdemo.database.AppDatabase
import it.consoft.codescannerdemo.viewModels.CodeGeneratorViewModel
import it.consoft.codescannerdemo.viewModels.CodeRepositoryViewModel
import it.consoft.codescannerdemo.viewModels.CodeScannerViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.dsl.module
import org.koin.core.module.dsl.viewModelOf

class CodeScannerApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        val dbInstance = AppDatabase.getInstance(applicationContext)
        val databaseModule = module {
            single {
                dbInstance
            }
            single { dbInstance.codeDao() }
        }

        /*val apiModules = module {
            single { AuthService(androidContext()) }
            single { FacilityService(androidContext()) }
            single { VoucherService(androidContext()) }
            single { UserService(androidContext()) }
        }*/

        val viewModelModules = module {

            viewModelOf(::CodeScannerViewModel)
            viewModelOf(::CodeGeneratorViewModel)
            viewModelOf(::CodeRepositoryViewModel)

        }

        startKoin {
            androidContext(this@CodeScannerApplication)
            modules(listOf(
                //apiModules,
                viewModelModules,
                databaseModule
            ))

        }
    }


}