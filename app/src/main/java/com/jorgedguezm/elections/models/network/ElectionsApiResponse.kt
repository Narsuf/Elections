package com.jorgedguezm.elections.models.network

import com.jorgedguezm.elections.models.Election
import com.jorgedguezm.elections.models.NetworkResponseModel

data class ElectionApiResponse (val elections: List<Election>) : NetworkResponseModel