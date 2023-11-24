package com.br.streamcontrol.domain.viewmodel

import android.Manifest
import android.app.Application
import android.content.pm.PackageManager
import android.location.Location
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.br.streamcontrol.data.remote.dto.response.LocationResponse
import com.br.streamcontrol.domain.repository.LocationRepositoryImpl
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LocationViewModel @Inject constructor(
    application: Application,
    private val repository: LocationRepositoryImpl,
) : AndroidViewModel(application) {
    private val fusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(application)

    private val locationData: MutableState<Location?> = mutableStateOf(null)

    private val _locationResult = MutableLiveData<Result<List<LocationResponse>>>()
    val locationResult: LiveData<Result<List<LocationResponse>>> = _locationResult

    val cityLiveData = MutableLiveData<String>()

    fun requestLocation() {
        if (ContextCompat.checkSelfPermission(
                getApplication(),
                Manifest.permission.ACCESS_FINE_LOCATION,
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationProviderClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    locationData.value = location
                    getCurrentLocation("${location?.latitude} ${location?.longitude}")
                }
        }
    }

    private fun getCurrentLocation(query: String) {
        viewModelScope.launch(Dispatchers.IO + CoroutineExceptionHandler { _, throwable ->
            Log.e("ERRO Location ", "$throwable")
            _locationResult.value = Result.failure(throwable)
        }) {
            runCatching {
                repository.getLocation(query)
            }.onSuccess { locationResponseList ->
                val firstLocationResponse = locationResponseList.getOrNull()?.first()
                val address = firstLocationResponse?.address
                val city = address?.city
                val addressState = firstLocationResponse?.address?.state
                val country = firstLocationResponse?.address?.country
                val location = "$city, $addressState, $country"
                cityLiveData.postValue(location)
            }.onFailure { throwable ->
                Log.e("ERRO Location ", "$throwable")
            }.let {
                _locationResult.postValue(it.getOrNull())
            }
        }
    }
}
