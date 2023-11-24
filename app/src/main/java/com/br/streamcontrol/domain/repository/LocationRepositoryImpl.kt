package com.br.streamcontrol.domain.repository

import com.br.streamcontrol.data.remote.dto.response.LocationResponse
import com.br.streamcontrol.data.remote.service.LocationService
import javax.inject.Inject

class LocationRepositoryImpl @Inject constructor(
    private var service: LocationService,
) : LocationRepository, BaseRepository() {

    override suspend fun getLocation(query: String): Result<List<LocationResponse>> {
        return safeApiCall {
            service.getUserLocation(query).execute()
        }.onSuccess {
            Result.success(it)
        }.onFailure { exception ->
            Result.failure<LocationResponse>(Exception("Erro ao tentar puxar a localização $exception"))
        }
    }
}