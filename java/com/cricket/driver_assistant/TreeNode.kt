package com.cricket.driver_assistant

import org.json.JSONObject

/*
JSON{
    id: 0
    text: ""
    description: ""
    parent_id: 0
    is_binary: 0
    current_state: 0
}
*/

class TreeNode(lObj: JSONObject?)
{
    var mObj = lObj
    var mLeft: TreeNode? = null
    var mRight: TreeNode? = null
}
