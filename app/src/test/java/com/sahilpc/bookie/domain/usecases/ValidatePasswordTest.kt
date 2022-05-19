package com.sahilpc.bookie.domain.usecases

import org.junit.Test
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import javax.inject.Inject


class ValidatePasswordTest {

    lateinit var validatePassword: ValidatePassword

    @Before
    fun setup(){
        validatePassword = ValidatePassword()
    }

    @Test
    fun `empty password returns false`() {
        val result = validatePassword(
            username = "User Name",
            password = "",
        )
        assertThat(result.successful).isFalse()
    }

    @Test
    fun `password length is less than 8 returns false`() {
        val result = validatePassword(
            username = "User Name",
            password = "",
        )
        assertThat(result.successful).isFalse()
    }

    @Test
    fun `password length is greater than 15 returns false`() {
        val result = validatePassword(
            username = "User Name",
            password = "Password@123456",
        )
        assertThat(result.successful).isFalse()
    }

    @Test
    fun `first character of password must be lowercase returns false`() {
        val result = validatePassword(
            username = "User Name",
            password = "Password@123456",
        )
        assertThat(result.successful).isFalse()
    }

    @Test
    fun `password must contains 2 digits returns false`() {
        val result = validatePassword(
            username = "User Name",
            password = "Password@ss",
        )
        assertThat(result.successful).isFalse()
    }

}