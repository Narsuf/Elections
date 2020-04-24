package com.jorgedguezm.elections.models.network

import com.jorgedguezm.elections.models.entities.Election
import com.jorgedguezm.elections.models.NetworkResponseModel

data class ElectionApiResponse (val elections: List<Election>) : NetworkResponseModel