package com.example.doan.DataSource

class Contacts(id :Long, name : String, number : String  ) {
    var contactId: Long? = id
    var userName: String? = name
    var contactNumber: String? = number

    constructor() : this(0,"","")

}
