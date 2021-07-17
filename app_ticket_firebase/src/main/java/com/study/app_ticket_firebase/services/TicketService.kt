package com.study.app_ticket_firebase.services

import com.study.app_ticket_firebase.models.Ticket
import com.study.app_ticket_firebase.models.TicketsStock

class TicketService {
    companion object {
        lateinit var errorMessages: Array<String>
    }
    // 購票提交
    fun submit(
        ticketsStock: TicketsStock,
        username: String,
        allTickets: Int,
        roundTrip: Int
    ): Ticket? {
        val checkNo = checkTicket(ticketsStock, username, allTickets, roundTrip)
        var ticket: Ticket? = null
        if (checkNo == 0) {
            val oneWay = allTickets - roundTrip * 2
            val total =
                ((allTickets - oneWay) * ticketsStock.discount + oneWay) * ticketsStock.price
            ticket = Ticket(username, allTickets, roundTrip, oneWay, total.toInt())
        } else
            throw Exception(errorMessages[checkNo])

        return ticket
    }

    // 檢驗票務資訊
    private fun checkTicket(
        ticketsStock: TicketsStock,
        username: String,
        allTickets: Int,
        roundTrip: Int
    ): Int {
        /* 1. 購買票數是否大於 0
           2. 剩餘票數是否足夠 ?
           3. 來回票數是否正確 ? */

        if (allTickets <= 0)
            return 1
        if (ticketsStock.totalAmount < allTickets)
            return 2
        if (roundTrip < 0 || roundTrip * 2 > allTickets)
            return 3

        return 0
    }
}