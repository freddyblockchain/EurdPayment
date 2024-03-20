package com.example.eurdpayment.Navigation

sealed class EurdPaymentScreen(val route: String) {
    data object SendMoneyScreen : EurdPaymentScreen("send_money_screen")
    data object PaymentScreen : EurdPaymentScreen("payment_screen")
    data object ReceiveScreen : EurdPaymentScreen("receive_screen")
}

val screens = listOf(
    EurdPaymentScreen.SendMoneyScreen,
    EurdPaymentScreen.ReceiveScreen,
)