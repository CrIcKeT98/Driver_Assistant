package com.cricket.driver_assistant

import android.content.Context
import android.util.Log
import android.widget.Toast
import java.util.*

abstract class ActionListInterface{
    abstract fun actionButtonListHandler(id: Int)

    companion object{
        var mStaticListManager: ActionListInterface? = null
    }
}

class ActionListManager(
    private val mContext: Context,
    private val mJsInterface: JsInterface,
    mStartId: Int,
    mText: String) : ActionListInterface() {

    private var mActionListTree = TreeNode(null)

    //--------------------------------------------------------------------------

    init {
        mJsInterface.setActionListTitle(mText)

        buildTree(mStartId)
        showTree(mActionListTree)
    }

    private fun buildTree(mStartId: Int){
        val lDb = DbManager(mContext, null)
        mActionListTree.mObj = lDb.getButtonById(mStartId)
        lDb.close()

        fillNode(mActionListTree)
    }

    private fun fillNode(lNode: TreeNode){
        val lDb = DbManager(mContext, null)
        val lData = lDb.getButtonsByParentId(lNode.mObj!!.getInt(DbManager.ID_COL))
        lDb.close()

        if(lData.isNotEmpty()){
            lNode.mLeft = TreeNode(lData[0])

            if(lData.size > 1)
                lNode.mRight = TreeNode(lData[1])
        }

        if(lNode.mLeft != null)
            fillNode(lNode.mLeft!!)

        if(lNode.mRight != null)
            fillNode(lNode.mRight!!)
    }

    private fun showTree(lNode: TreeNode){
        if(lNode.mLeft != null) {
            mJsInterface.createActionButton(lNode.mObj!!, false)
            showTree(lNode.mLeft!!)
        }
    }

    private fun findNodeById(id: Int) : TreeNode{
        val lStack = Stack<TreeNode>()
        var lNode: TreeNode? = null

        lStack.push(mActionListTree)

        while(lStack.isNotEmpty()){
            lNode = lStack.pop()

            if(lNode.mObj!!.getInt(DbManager.ID_COL) == id)
                return lNode

            if(lNode.mLeft != null)
                lStack.push(lNode.mLeft)

            if(lNode.mRight != null)
                lStack.push(lNode.mRight)
        }

        return lNode!!
    }

    override fun actionButtonListHandler(id: Int){
        val lNode = findNodeById(id)
        var lCurState = lNode.mObj!!.getInt("current_state")
        lCurState = (lCurState + 1) % 3
        lNode.mObj!!.put("current_state", lCurState)

        Log.d("debug-tag", lCurState.toString())
        mJsInterface.setActionButtonState(lCurState)
    }
}
