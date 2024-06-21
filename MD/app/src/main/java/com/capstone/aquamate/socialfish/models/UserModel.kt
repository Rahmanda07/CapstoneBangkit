package com.capstone.aquamate.socialfish.models

class UserModel {
    var image: String? = null
    var name: String? = null
    var email: String? = null
    var password: String? = null
    var bio: String? = null
    var fullname: String? = null

    constructor()

    constructor(image: String?, name: String?, email: String?, password: String?, bio: String?, fullname: String?) {
        this.image = image
        this.name = name
        this.email = email
        this.password = password
        this.bio = bio
        this.fullname = fullname
    }

    constructor(name: String?, email: String?, password: String?) {
        this.name = name
        this.email = email
        this.password = password
    }

    constructor(email: String?, password: String?) {
        this.email = email
        this.password = password
    }
}