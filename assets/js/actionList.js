function setActionListTitle(text){
    const container = document.querySelector('.container');

    let titleText = document.createElement('h5');
    titleText.innerText = text;

    container.insertBefore(titleText,  container.firstChild)
}

function createActionButton(obj, isLast){
    const lButtonList = document.querySelector('.action-button-list');

    let lButton = document.createElement('a');
    lButton.className = "waves-effect waves-light btn";
    lButton.innerHTML = obj.text;
    lButton.setAttribute("button_id", obj.id);
    lButtonList.append(lButton);

    if(!isLast){
        let lArrow = document.createElement('i');
        lArrow.className = "fa-solid fa-caret-down action-list-arrow";
        lButton.addEventListener('click', actionButtonListListener);
        lButtonList.append(lArrow);
    }
}

function actionButtonListListener(e){
    const lId = e.srcElement.getAttribute("button_id");
    console.log(lId);
    Android.actionButtonListListener(lId);
}

function setActionButtonState(newState){
    console.log("js debug:", newState);
    let oldState = 1;
    const lActionButton = document.querySelector(`a[button_id="${oldState}"]`);
    lActionButton.textContent = "ASDASDASDASD";
}