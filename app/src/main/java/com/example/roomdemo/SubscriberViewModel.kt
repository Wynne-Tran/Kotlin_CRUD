package com.example.roomdemo

import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.roomdemo.db.Subscriber
import com.example.roomdemo.db.SubscriberRepository
import kotlinx.coroutines.launch

class SubscriberViewModel(private val repository: SubscriberRepository) : ViewModel(), Observable{
    //call variable from repository
    val subscribers = repository.subscribers

    // when use bindable data we have to add code inside activity_main
    @Bindable
    val inputName = MutableLiveData<String?>()
    @Bindable
    val inputEmail = MutableLiveData<String?>()
    @Bindable
    val saveOrUpdateText = MutableLiveData<String>()
    @Bindable
    val clearOrDeleteText = MutableLiveData<String>()

    init {
        saveOrUpdateText.value = "Save"
        clearOrDeleteText.value = "Clear"
    }

    // to run this func, create "onClick" in main_activity

    fun saveOrUpdate(){
        val name = inputName.value!!
        val email = inputEmail.value!!
        insert(Subscriber(0, name, email))
        inputName.value = null
        inputEmail.value = null
    }

    fun clearAllOrDelete(){
        deleteAll()
    }

    fun insert(subscriber: Subscriber) {
        viewModelScope.launch { repository.insert(subscriber) }
    }
    fun update (subscriber: Subscriber) {
        viewModelScope.launch { repository.update(subscriber) }
    }
    fun delete (subscriber: Subscriber) {
        viewModelScope.launch { repository.delete(subscriber) }
    }
    fun deleteAll() {
        viewModelScope.launch { repository.deleteAll() }
    }

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
    }
}