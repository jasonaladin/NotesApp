package com.jasonaladin.notesapp

class Note(){
    var nodeID:Int? = null
    var nodeNote:String? = null
    var nodeTitle:String? = null

    constructor(nodeID:Int, nodeName:String, nodeDes:String) : this() {
        this.nodeID = nodeID
        this.nodeTitle = nodeName
        this.nodeNote = nodeDes
    }

}