package com.sgrh.humor

data class Post(
    var id: Int ,
    var filename: String ,
    var description: String,
    var timeposted: String,
    var isNsfw: Boolean,
    var displayname: String,
    var isHidden: Boolean
){
    override fun toString(): String {
        return "PostResponse(id=$id, filename='$filename', description='$description', timeposted='$timeposted', isNsfw=$isNsfw, displayname='$displayname', isHidden=$isHidden)"
    }
}
