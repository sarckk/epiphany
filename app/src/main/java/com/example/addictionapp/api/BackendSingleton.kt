package com.example.addictionapp.api;

public class BackendSingleton {

    var backend: BackendService? = null

    public fun getInstance() : BackendService {
        if (backend == null) {
            backend = BackendService.create()
        }

        return backend as BackendService
    }
}
