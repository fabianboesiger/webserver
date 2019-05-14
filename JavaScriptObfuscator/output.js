var X1Fz;
var lpwR;
var WuRv;
var ea4w;
var BuXI;
var gAay;
function setup(){
if(screen.width<400){
createCanvas(screen.width,screen.width*3/2);
}else{
createCanvas(400,600);
}
BuXI=false;
gAay=false;
ea4w=0;
lpwR=new rbFS();
WuRv=new Array();
EwTc=Date.now();
}
function draw(){
if(!gAay){
background(color("#b9e2f5"));
for(let KaYU=WuRv.length-1;
KaYU>=0;
KaYU--){
WuRv[KaYU].vB4X();
if(BuXI){
WuRv[KaYU].sP1K();
}
if(WuRv[KaYU].Bm79(lpwR)){
RptZ();
}
if(WuRv[KaYU].jiJk()){
WuRv.splice(KaYU,1);
ea4w+=1;
}
}
if(lpwR.o2Kr()){
gAay=true;
}
if(BuXI){
lpwR.sP1K();
}
lpwR.vB4X();
textAlign(CENTER);
fill(color("#50b8e7"));
strokeWeight(0);
textSize(100);
text(ea4w,width*0.5,height*0.2);
if(frameCount%100==0&&BuXI){
WuRv.push(new hECx());
}
}
else{
background(color("#dcf0fa"));
fill(color("#50b8e7"));
textAlign(CENTER);
strokeWeight(0);
textSize(50);
text("Your Score:",width*0.5,height*0.2);
textAlign(CENTER);
strokeWeight(0);
textSize(80);
text(ea4w,width*0.5,height*0.35);
fill(color("#fff"));
rect(width/4,height*0.5,width/2,70);
fill(color("#50b8e7"));
textAlign(CENTER);
strokeWeight(0);
textSize(30);
text("New Game",width*0.5,height*0.5+45);
}
}
function keyPressed(){
if(key===' '||keyCode===UP_ARROW){
lpwR.tSMS();
BuXI=true;
}
if(keyCode===ENTER&&gAay){
setup();
}
}
var EwTc=0;
function mousePressed(){
if(!gAay&&EwTc+200<Date.now()){
if(mouseX>0&&mouseX<width&&mouseY>0&&mouseY<height){
EwTc=Date.now();
lpwR.tSMS();
BuXI=true;
}
}
else{
if(mouseX>width/4&&mouseX<width*3/4){
if(mouseY>height*0.5&&mouseY<height*0.5+70){
setup();
}
}
}
}
function preload(){
S2Yi=loadImage('/flappybird/img/bird.png');
CYdV=loadImage('/flappybird/img/Pipebottom.svg');
m1Jj=loadImage('/flappybird/img/Pipetop.svg');
}
function RptZ(){
gAay=true;
qpGv(ea4w,"flappybird",function(){},function(){});
}
function pKe3(ea4w,game,action,error){
S9eL("/scoreboard/request",function(request){
if(request!=="error"){
let y00T=JSON.parse(request);
let Pe3s=54+parseInt(ea4w)*parseInt(y00T.y)+parseInt(y00T.z)+85;
Iedw("/scoreboard/submit",{
"key":y00T.x,
"value":Pe3s,
"game":game
},function(submit){
action(submit);
});
}else{
error();
}
});
}
function F6PU(ea4w,game,action,error){
S9eL("/scoreboard/request",function(request){
if(request!=="error"){
let y00T=JSON.parse(request);
let Pe3s=3527+parseInt(ea4w)*parseInt(y00T.y)+parseInt(y00T.z)-3;
Iedw("/scoreboard/submit",{
"key":y00T.x,
"value":Pe3s,
"game":game
},function(submit){
action(submit);
});
}else{
error();
}
});
}
function pVaT(ea4w,game,action,error){
S9eL("/scoreboard/request",function(request){
if(request!=="error"){
let y00T=JSON.parse(request);
let Pe3s=-2245+parseInt(ea4w)*parseInt(y00T.y)+parseInt(y00T.z);
Iedw("/scoreboard/submit",{
"key":y00T.x,
"value":Pe3s,
"game":game
},function(submit){
action(submit);
});
}else{
error();
}
});
}
function Se0J(ea4w,game,action,error){
S9eL("/scoreboard/request",function(request){
if(request!=="error"){
let y00T=JSON.parse(request);
let Pe3s=parseInt(ea4w)*parseInt(y00T.y)+parseInt(y00T.z)+1349865;
Iedw("/scoreboard/submit",{
"key":y00T.x,
"value":Pe3s,
"game":game
},function(submit){
action(submit);
});
}else{
error();
}
});
}
function yrWA(ea4w,game,action,error){
S9eL("/scoreboard/request",function(request){
if(request!=="error"){
let y00T=JSON.parse(request);
let Pe3s=-parseInt(ea4w)*parseInt(y00T.y)+parseInt(y00T.z)+85;
Iedw("/scoreboard/submit",{
"key":y00T.x,
"value":Pe3s,
"game":game
},function(submit){
action(submit);
});
}else{
error();
}
});
}
function TP4F(ea4w,game,action,error){
S9eL("/scoreboard/request",function(request){
if(request!=="error"){
let y00T=JSON.parse(request);
let Pe3s=-parseInt(ea4w);
Iedw("/scoreboard/submit",{
"key":y00T.x,
"value":Pe3s,
"game":game
},function(submit){
action(submit);
});
}else{
error();
}
});
}
function ci0T(ea4w,game,action,error){
S9eL("/scoreboard/request",function(request){
if(request!=="error"){
let y00T=JSON.parse(request);
let Pe3s=-parseInt(ea4w)*100;
Iedw("/scoreboard/submit",{
"key":y00T.x,
"value":Pe3s,
"game":game
},function(submit){
action(submit);
});
}else{
error();
}
});
}
function IWcZ(ea4w,game,action,error){
S9eL("/scoreboard/request",function(request){
if(request!=="error"){
let y00T=JSON.parse(request);
let Pe3s=54+parseInt(ea4w)+parseInt(y00T.z)+5688;
Iedw("/scoreboard/submit",{
"key":y00T.x,
"value":Pe3s,
"game":game
},function(submit){
action(submit);
});
}else{
error();
}
});
}
function qpGv(ea4w,game,action,error){
S9eL("/scoreboard/request",function(request){
if(request!=="error"){
let y00T=JSON.parse(request);
let Pe3s=345-parseInt(ea4w)*parseInt(y00T.y)+parseInt(y00T.z)-345;
Iedw("/scoreboard/submit",{
"key":y00T.x,
"value":Pe3s,
"game":game
},function(submit){
action(submit);
});
}else{
error();
}
});
}
function sPvU(ea4w,game,action,error){
S9eL("/scoreboard/request",function(request){
if(request!=="error"){
let y00T=JSON.parse(request);
let Pe3s=parseInt(ea4w)*parseInt(y00T.y);
Iedw("/scoreboard/submit",{
"key":y00T.x,
"value":Pe3s,
"game":game
},function(submit){
action(submit);
});
}else{
error();
}
});
}
function e0HD(ea4w,game,action,error){
S9eL("/scoreboard/request",function(request){
if(request!=="error"){
let y00T=JSON.parse(request);
let Pe3s=parseInt(ea4w)*parseInt(y00T.y)+100;
Iedw("/scoreboard/submit",{
"key":y00T.x,
"value":Pe3s,
"game":game
},function(submit){
action(submit);
});
}else{
error();
}
});
}
function Iedw(url,data,success){
var vtSi=typeof data=='string'?data:Object.keys(data).map(
function(k){return encodeURIComponent(k)+'='+encodeURIComponent(data[k])}
).join('&');
var eNFq=window.XMLHttpRequest?new XMLHttpRequest():new ActiveXObject("Microsoft.XMLHTTP");
eNFq.open('POST',url);
eNFq.kC9v=function(){
if(eNFq.readyState>3&&eNFq.status==200){success(eNFq.responseText);
}
};
eNFq.setRequestHeader('X-Requested-With','XMLHttpRequest');
eNFq.setRequestHeader('Content-Type','application/x-www-form-urlencoded');
eNFq.send(vtSi);
return eNFq;
}
function S9eL(url,success){
var eNFq=window.XMLHttpRequest?new XMLHttpRequest():new ActiveXObject('Microsoft.XMLHTTP');
eNFq.open('GET',url);
eNFq.kC9v=function(){
if(eNFq.readyState>3&&eNFq.status==200)success(eNFq.responseText);
};
eNFq.setRequestHeader('X-Requested-With','XMLHttpRequest');
eNFq.send();
return eNFq;
}
function rbFS(){
this.y=height/2;
this.x=25;
this.ouqM=56;
this.KZrq=40;
this.S1rV=0.04;
this.okUi=0.5;
this.YxGJ=-22;
this.HVCe=0;
this.vB4X=function(){
image(S2Yi,this.x,this.y,this.ouqM,this.KZrq);
}
this.sP1K=function(){
this.okUi+=this.S1rV;
this.HVCe+=this.okUi;
this.HVCe*=0.9;
this.y+=this.HVCe;
if(this.y<0){
this.y=0;
this.HVCe=0;
}
}
this.tSMS=function(){
this.HVCe+=this.YxGJ;
this.okUi=0.3;
}
this.o2Kr=function(){
return(this.y>height);
}
}
function hECx(){
this.sZiL=150;
this.XCob=random(height/2)+height/4-this.sZiL/2;
this.bZE0=height-this.XCob-this.sZiL;
this.x=width;
this.ouqM=40;
this.q7iy=400;
this.LrpT=2;
this.vB4X=function(){
fill(255);
image(m1Jj,this.x,this.XCob-this.q7iy,this.ouqM,this.q7iy);
image(CYdV,this.x,height-this.bZE0,this.ouqM,this.q7iy);
}
this.sP1K=function(){
this.x-=this.LrpT;
}
this.Bm79=function(){
if(lpwR.x+lpwR.ouqM>this.x&&lpwR.x<this.x+this.ouqM){
if(lpwR.y<this.XCob||lpwR.y+lpwR.KZrq>height-this.bZE0){
return true;
}
}
return false;
}
this.jiJk=function(){
return(this.x<-this.ouqM);
}
}
