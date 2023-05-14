package com.cricket.driver_assistant

import android.content.Context
import android.graphics.Color.red
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

    private fun showTree(lNode: TreeNode){
        var lIsBinary = lNode.mObj!!.getInt(DbManager.IS_BINARY_COL)
        var lCurState = lNode.mObj!!.getInt("current_state")
        var lColor = String()

        if(lCurState == 1)
            lColor = getColorAsString(R.color.limegreen)
        else if(lCurState == 2)
            lColor = getColorAsString(R.color.red)

        mJsInterface.createActionButton(lNode.mObj!!, lColor, false)

        if(lIsBinary == 0 && lNode.mLeft != null)
            showTree(lNode.mLeft!!)
        else if(lIsBinary == 1 && lCurState == 1 && lNode.mLeft != null)
            showTree(lNode.mLeft!!)
        else if(lIsBinary == 1 && lCurState == 2 && lNode.mRight != null)
            showTree(lNode.mRight!!)
    }

    private fun reShowTree(){
        mJsInterface.clearActionButtonList();
        showTree(mActionListTree)
    }

    override fun actionButtonListHandler(id: Int){
        val lNode = findNodeById(id)
        var lCurState = lNode.mObj!!.getInt("current_state")
        val lIsBinary = lNode.mObj!!.getInt(DbManager.IS_BINARY_COL)

         if(lIsBinary == 0) {
             lCurState = ++lCurState % 2

             if(lCurState == 0)
                mJsInterface.setActionButtonColor(id, "")
             else
                 mJsInterface.setActionButtonColor(id, getColorAsString(R.color.limegreen))
         }
        else {
             lCurState = ++lCurState % 3
             lNode.mObj!!.put("current_state", lCurState)
             reShowTree()
             return
         }

        lNode.mObj!!.put("current_state", lCurState)
    }

    private fun getColorAsString(id: Int): String{
        return "#" + Integer.toHexString(mContext.getColor(id) and 0x00ffffff)
    }
}
