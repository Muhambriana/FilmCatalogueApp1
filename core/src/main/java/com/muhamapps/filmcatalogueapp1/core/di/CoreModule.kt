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
import net.zetetic.database.sqlcipher.SQLiteDatabase
import net.zetetic.database.sqlcipher.SupportOpenHelperFactory
import okhttp3.CertificatePinner
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
        val passphrase: ByteArray = "dicoding".toByteArray(Charsets.UTF_8)
        val factory = SupportOpenHelperFactory(passphrase)
        Room.databaseBuilder(
            androidContext(),
            FilmDatabase::class.java, "Film.db"
        ).fallbackToDestructiveMigration().openHelperFactory(factory).build()
    }
}

val networkModule = module {
    single {
        val hostname = "api.themoviedb.org"
        val certificatePinner = CertificatePinner.Builder()
            .add(hostname, "sha256/f78NVAesYtdZ9OGSbK7VtGQkSIVykh3DnduuLIJHMu4=")
            .add(hostname, "sha256/G9LNNAql897egYsabashkzUCTEJkWBzgoEtk8X/678c=")
            .add(hostname, "sha256/++MBgDH5WGvL9Bcn5Be30cRcL0f5O+NyoXuWtQdX1aI=")
            .build()
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .certificatePinner(certificatePinner)
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