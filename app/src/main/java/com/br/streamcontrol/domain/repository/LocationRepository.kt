package com.br.streamcontrol.domain.repository

import com.br.streamcontrol.data.remote.dto.response.LocationResponse

interface LocationRepository {
    suspend fun getLocation(query: String): Result<List<LocationResponse>>
}