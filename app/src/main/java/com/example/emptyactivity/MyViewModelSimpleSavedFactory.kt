package com.example.emptyactivity

import androidx.lifecycle.*

/* ViewModel Factory that will create our view model by injecting the
 ProfileDataStore from the module.
*/
class MyViewModelSimpleSavedFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MyViewModelSimpleSaved(MyApp.appModule.profileRepository) as T
    }
}
