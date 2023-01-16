package com.lee.hilttodolist

import com.lee.hilttodolist.Interface.MainApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class HiltModule {

    @Provides
    fun provideMainRepository(mainApiService: MainApiService) = MainRepository(mainApiService)
}
