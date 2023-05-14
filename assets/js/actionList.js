function setActionListTitle(text){
    const container = document.querySelector('.container');

    let titleText = document.createElement('h5');
    titleText.innerText = text;

    container.insertBefore(titleText,  container.firstChild)
}

function createActionButton(lObj, lColor, isLast){
    const lButtonList = document.querySelector('.action-button-list');

    let lButton = document.createElement('a');
    lButton.className = "waves-effect waves-light btn";
    lButton.innerHTML = lObj.text;
    lButton.setAttribute("button_id", lObj.id);

    console.log("jsDebug: ", lColor)
    if(lColor != '')
        lButton.style.setProperty('background-color', lColor)

    lButtonList.append(lButton);

    if(!isLast){
        let lArrow = document.createElement('i');
        lArrow.className = "fa-solid fa-caret-down action-list-arrow";
        lButton.addEventListener('click', actionButtonListListener);
        lButtonList.append(lArrow);
    }
}

function clearActionButtonList(){
    const lButtonList = document.querySelector('.action-button-list');
    lButtonList.replaceChildren();
}

function actionButtonListListener(e){
    const lId = e.srcElement.getAttribute("button_id");
    Android.actionButtonListListener(lId);
}

function setActionButtonColor(lId, lColor){
    const lActionButton = document.querySelector(`a[button_id="${lId}"]`);
    
    if(lColor.length == 0)
        lActionButton.removeAttribute("style")
    else
        lActionButton.style.setProperty('background-color', lColor);
}
