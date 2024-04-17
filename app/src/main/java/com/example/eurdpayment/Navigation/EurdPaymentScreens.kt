package com.example.eurdpayment.Navigation

sealed class EurdPaymentScreen(val route: String) {
    data object SendMoneyScreen : EurdPaymentScreen("send_money_screen")
    data object PaymentScreen : EurdPaymentScreen("payment_screen")
    data object ReceiveScreen : EurdPaymentScreen("receive_screen")

    data object HistoryScreen : EurdPaymentScreen("History")
}

val screens = listOf(
    EurdPaymentScreen.HistoryScreen,
    EurdPaymentScreen.SendMoneyScreen,
    EurdPaymentScreen.ReceiveScreen,

)