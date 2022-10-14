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
    // 2 variable to update and delete
    private var isUpdateOrDelete = false
    private lateinit var subcriberToUpdateOrDelete : Subscriber

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
        if(isUpdateOrDelete){
            subcriberToUpdateOrDelete.name = inputName.value!!
            subcriberToUpdateOrDelete.email = inputEmail.value!!
            update(subcriberToUpdateOrDelete)

        }
        else {
            val name = inputName.value!!
            val email = inputEmail.value!!
            insert(Subscriber(0, name, email))
            inputName.value = null
            inputEmail.value = null
        }
    }

    fun clearAllOrDelete(){
        if(isUpdateOrDelete) {
            delete(subcriberToUpdateOrDelete)
        }
        else {
            deleteAll()
        }
    }

    fun insert(subscriber: Subscriber) {
        viewModelScope.launch { repository.insert(subscriber) }
    }
    fun update (subscriber: Subscriber) {
        viewModelScope.launch {
            repository.update(subscriber)
            inputName.value = null
            inputEmail.value = null
            isUpdateOrDelete = false
            saveOrUpdateText.value = "Save"
            clearOrDeleteText.value = "Clear All"
        }
    }
    fun delete (subscriber: Subscriber) {
        viewModelScope.launch {
            repository.delete(subscriber)
            inputName.value = null
            inputEmail.value = null
            isUpdateOrDelete = false
            saveOrUpdateText.value = "Save"
            clearOrDeleteText.value = "Clear All"
        }
    }
    fun deleteAll() {
        viewModelScope.launch { repository.deleteAll() }
    }

    fun initUpdateAndDelete( subscriber: Subscriber) {
        inputName.value = subscriber.name
        inputEmail.value = subscriber.email
        isUpdateOrDelete = true
        subcriberToUpdateOrDelete = subscriber
        saveOrUpdateText.value = "Update"
        clearOrDeleteText.value = "Delete"
    }

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
    }
}