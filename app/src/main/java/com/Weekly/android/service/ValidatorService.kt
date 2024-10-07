package com.Weekly.android.service

import android.util.Patterns
import com.Weekly.android.model.Request.LoginRequest
import com.Weekly.android.model.Request.SignupRequest

class ValidatorService {

    object SignupValidator{
        @JvmStatic
        fun isNotEmpty(signupRequest: SignupRequest): Boolean {
            return signupRequest.email!=""&&
                    signupRequest.username!=""&&
                    signupRequest.password!=""
        }
    }

    object EmailValidator{
        @JvmStatic
        fun isValid(email:String):Boolean{
            return Patterns.EMAIL_ADDRESS.matcher(email).matches()
        }
    }

    object LoginValidator{
        @JvmStatic
        fun isNotEmpty(loginRequest: LoginRequest):Boolean{
            return loginRequest.email!=""&&loginRequest.password!=""
        }
    }

    object CostValidator{
        @JvmStatic
        fun isPositiveAndDouble(cost: String):Boolean{
            return cost.toDoubleOrNull() != null && cost.toDouble() > 0
        }
    }
}