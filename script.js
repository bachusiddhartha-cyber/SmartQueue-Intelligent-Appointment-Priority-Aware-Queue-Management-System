let queue = JSON.parse(localStorage.getItem("queue")) || [];

let avgConsultTime = 5;
let waitingBoostFactor = 0.5;

if("Notification" in window){
Notification.requestPermission();
}

function saveQueue(){
localStorage.setItem("queue",JSON.stringify(queue));
}

function addPatient(){

let name=document.getElementById("name").value;
let baseScore=parseInt(document.getElementById("category").value);

if(name===""){
alert("Enter name");
return;
}

queue.push({
name:name,
baseScore:baseScore,
waitingTime:0,
notify:false
});

updateScores();
saveQueue();

document.getElementById("name").value="";
}

function updateScores(){

queue.forEach(patient=>{
patient.totalScore=patient.baseScore+(patient.waitingTime*waitingBoostFactor);
});

queue.sort((a,b)=>b.totalScore-a.totalScore);

calculateEstimation();
displayQueue();
}

function calculateEstimation(){

let time=0;

queue.forEach(patient=>{
patient.estimated=time;
time+=avgConsultTime;
});

}

function displayQueue(){

let table=document.getElementById("queueTable");
table.innerHTML="";

queue.forEach((patient,index)=>{

table.innerHTML+=`

<tr>
<td>${patient.name}</td>
<td>${patient.baseScore}</td>
<td>${patient.waitingTime}</td>
<td>${patient.totalScore.toFixed(1)}</td>
<td>${patient.estimated} mins</td>

<td>
<button onclick="enableNotify(${index})">
Notify
</button>
</td>
</tr>

`;

});

}

function enableNotify(index){

queue[index].notify=true;

alert("Notification enabled for "+queue[index].name);

}

function callNext(){

if(queue.length===0){
alert("Queue empty");
return;
}

queue.shift();

queue.forEach(patient=>{
patient.waitingTime+=avgConsultTime;
});

updateScores();
saveQueue();

if(queue.length>0 && queue[0].notify){
sendNotification(queue[0].name);
}

}

function sendNotification(name){

if(Notification.permission==="granted"){

new Notification("SmartQueue Alert",{
body:name+" your consultation is about to start"
});

}

}

async function loadHealthTip(){

try{

let response=await fetch("https://api.adviceslip.com/advice");
let data=await response.json();

console.log("Health Tip:",data.slip.advice);

}

catch(error){

console.log("API error");

}

}

loadHealthTip();

updateScores();