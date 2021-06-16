package com.muhamapps.filmcatalogueapp1.core.di

import androidx.room.Room
import com.muhamapps.filmcatalogueapp1.core.data.FilmRepository
import com.muhamapps.filmcatalogueapp1.core.data.source.local.LocalDataSource
import com.muhamapps.filmcatalogueapp1.core.data.source.local.room.FilmDatabase
import com.muhamapps.filmcatalogueapp1.core.data.source.remote.RemoteDataSource
import com.muhamapps.filmcatalogueapp1.core.data.source.remote.network.ApiService
import com.muhamapps.filmcatalogueapp1.core.domain.repository.IFilmRepository
import com.muhamapps.filmcatalogueapp1.core.utils.AppExecutors
import com.muhamapps.filmcatalogueapp1.core.utils.NetworkInfo.BASE_URL
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SupportFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
val databaseModule = module {
    factory { get<FilmDatabase>().filmDao() }
    single {
        val passphrase: ByteArray = SQLiteDatabase.getBytes("dicoding".toCharArray())
        val factory = SupportFactory(passphrase)
        Room.databaseBuilder(
            androidContext(),
            FilmDatabase::class.java, "Film.db"
        ).fallbackToDestructiveMigration().openHelperFactory(factory).build()
    }
}

val networkModule = module {
    single {
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .build()
    }
    single {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(get())
            .build()
        retrofit.create(ApiService::class.java)
    }
}

val repositoryModule = module {
    single { LocalDataSource(get()) }
    single { RemoteDataSource(get()) }
    factory { AppExecutors() }
    single<IFilmRepository> {
        FilmRepository(
            get(),
            get(),
            get()
        )
    }
}