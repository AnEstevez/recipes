package com.andresestevez.data.repository

class NoDataFoundException(override val message: String = "No data found") : Exception()