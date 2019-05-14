var MCML=MCML+MCML;
function pGOZ(Kz54,fAZO,HFLF){
}
var ApG3;
var NFej;
var Owal;
function WElX(nM0z,uG6n){
}
var D5mU;
function hBOO(){
function FjhM(XrrN,vABi){
}function Uh2u(){
var tr7v=-1899363753;
function LLWU(v94k,d1NR){
}}}
var Mq2S;
var KdJ3;
function setup(){
if(screen.width<400){
createCanvas(screen.width,screen.width*3/2);
}else{
createCanvas(400,600);
}
Mq2S=false;
KdJ3=false;
D5mU=0;
function zPHP(wMTP,FGGN,qq9C){
}
NFej=new NpVj();
Owal=new Array();
Nz0P=Date.now();
}
function draw(){
if(!KdJ3){
background(color("#b9e2f5"));
function k2VA(mYvG){
}
for(let NTIt=Owal.length-1;
NTIt>=0;
NTIt--){
Owal[NTIt].tYo1();
if(Mq2S){
Owal[NTIt].cBOK();
}
if(Owal[NTIt].ja1U(NFej)){
CH93();
}
if(Owal[NTIt].GIVc()){
Owal.splice(NTIt,1);
D5mU+=1;
}
}
if(NFej.oEb1()){
KdJ3=true;
}
if(Mq2S){
NFej.cBOK();
var YmM5=YmM5;
var YmM5=YmM5+YmM5;
function M9I1(JiE4,MSVZ){
}if(YmM5<=YmM5){
}if(YmM5!=YmM5){
}function UHlB(Kvfm,monR){
var bHkY=Kvfm;
var bHkY=YmM5+YmM5;
function SsOo(MG4S,jVhj,zNY6){
var fnuG=true;
function hhZ8(gFCf){
}}}
}
NFej.tYo1();
function ybSH(heQM,JhoK,rH6L,rV2R,k69t,LIgI,YWBe){
}
textAlign(CENTER);
var yIAl=yIAl;
var yIAl=yIAl+yIAl;
function CHME(ovXb){
}function Ts5A(dUhm){
}
fill(color("#50b8e7"));
strokeWeight(0);
textSize(100);
text(D5mU,width*0.5,height*0.2);
if(frameCount%100==0&&Mq2S){
Owal.push(new u4bE());
}
}
else{
background(color("#dcf0fa"));
fill(color("#50b8e7"));
textAlign(CENTER);
function pLyH(dOXh){
var N58U=false;
function RH2s(){
var VJM7=-82549112;
function Xznz(){
}}}var GeF4=270170558;
function gS1X(baUm,E9HL){
}function Cszy(nh7K,iqVU){
}
strokeWeight(0);
var ortF=0.43381566;
function PUDZ(){
}
textSize(50);
text("Your Score:",width*0.5,height*0.2);
textAlign(CENTER);
strokeWeight(0);
textSize(80);
text(D5mU,width*0.5,height*0.35);
fill(color("#fff"));
function Dt4m(){
}var ijd3=ijd3;
var ijd3=ijd3+ijd3;
function E7ZE(){
var g5Ua=ijd3+g5Ua;
function ANk3(){
}}function iKHn(){
var FmM1=0.49832648;
function K7xC(M26v){
}}if(ijd3>=ijd3){
var uTPZ=ijd3;
var uTPZ=uTPZ+ijd3;
function kzjR(KBpF){
}}var rF7s=false;
function PBq7(FdwQ){
var jIJa=-1817658016;
function We5X(TTjr){
var T3BE=0.29191583;
function mTKc(y0aJ,CpCG,qtLy,DQuh){
if(y0aJ<=rF7s){
}}}}var PbpW=PbpW;
var PbpW=rF7s+PbpW;
function NXS2(R2la,UapZ){
}function gBNL(){
if(PbpW==PbpW){
}}
rect(width/4,height*0.5,width/2,70);
fill(color("#50b8e7"));
textAlign(CENTER);
strokeWeight(0);
function riID(){
}
textSize(30);
function dIwR(){
}
text("New Game",width*0.5,height*0.5+45);
}
}
function keyPressed(){
if(key===' '){
NFej.mUOd();
Mq2S=true;
}
if(keyCode===ENTER&&KdJ3){
setup();
}
}
var Nz0P=0;
function mousePressed(){
if(!KdJ3&&Nz0P+200<Date.now()){
if(mouseX>0&&mouseX<width&&mouseY>0&&mouseY<height){
Nz0P=Date.now();
NFej.mUOd();
var to4p=644314744;
function Hui2(){
}
Mq2S=true;
}
}
else{
if(mouseX>width/4&&mouseX<width*3/4){
if(mouseY>height*0.5&&mouseY<height*0.5+70){
setup();
function eVIk(lwu1,V9gw){
}
}
}
}
}
function preload(){
pAzG=loadImage('/img/bird.png');
leJh=loadImage('/img/Pipebottom.svg');
nCd8=loadImage('/img/Pipetop.svg');
var C6pz=false;
function AVwq(W1VB,f8s9,EfDQ,ylV6){
}
}
function CH93(){
KdJ3=true;
var qchU=-381585071;
function xgYS(ytoW){
}
JLwn(D5mU,"flappybird",function(){},function(){});
function jolB(){
}
}
function JLwn(D5mU,game,action,error){
mFH8("/scoreboard/request",function(request){
if(request!=="error"){
let yeXw=JSON.parse(request);
let rFZr=parseInt(D5mU)*parseInt(yeXw.y)+parseInt(yeXw.z);
Y3HX("/scoreboard/submit",{
"key":yeXw.x,
"value":rFZr,
"game":game
},function(submit){
action(submit);
});
}else{
error();
}
});
var Md75=-1195912701;
function C7Wr(khoZ,YTim){
}
}
function Y3HX(url,data,success){
var F8V8=typeof data=='string'?data:Object.keys(data).map(
function(k){return encodeURIComponent(k)+'='+encodeURIComponent(data[k])}
).join('&');
var cwSi=window.XMLHttpRequest?new XMLHttpRequest():new ActiveXObject("Microsoft.XMLHTTP");
cwSi.open('POST',url);
cwSi.vUAE=function(){
if(cwSi.readyState>3&&cwSi.status==200){success(cwSi.responseText);
}
};
function dlpu(xs7g){
}
cwSi.setRequestHeader('X-Requested-With','XMLHttpRequest');
cwSi.setRequestHeader('Content-Type','application/x-www-form-urlencoded');
cwSi.send(F8V8);
return cwSi;
function IGOa(){
var yqUF=1578110953;
function x3j8(MMc4){
}}
}
function mFH8(url,success){
var cwSi=window.XMLHttpRequest?new XMLHttpRequest():new ActiveXObject('Microsoft.XMLHTTP');
function K57N(eTB3,VV19){
}
cwSi.open('GET',url);
var OD79=false;
function pJ22(){
}function lKXq(pf9o){
}
cwSi.vUAE=function(){
if(cwSi.readyState>3&&cwSi.status==200)success(cwSi.responseText);
function Rsln(e2GY,KirW,r4fn,udCX){
}
};
function WANV(){
var dFBz=0.020604134;
function PWn1(YXlc){
if(dFBz!==dFBz){
}}if(dFBz>=dFBz){
var tUcM=tUcM+dFBz;
function EI0r(){
var VRg7=0.49758154;
function ZvOl(Uo64,L0S2){
}}}if(dFBz<dFBz){
}}
cwSi.setRequestHeader('X-Requested-With','XMLHttpRequest');
var mcfe=0.5951709;
function vYY9(){
var RVlQ=RVlQ;
var RVlQ=RVlQ+mcfe;
function qKtN(nLND,zWb6,knNk){
}}function uDB3(){
}
cwSi.send();
return cwSi;
}
function NpVj(){
this.y=height/2;
this.x=25;
this.JGct=56;
function MYnS(){
}
this.vJD9=40;
var h14P=false;
function zX2c(){
function ReCg(){
var Mhvy=Mhvy+h14P;
function xFPP(){
}}if(h14P<=h14P){
}var CxIw=h14P+h14P;
function jMr8(){
}}
this.mKyq=0.04;
function vMX1(){
}function ZUUS(){
}
this.xwFl=0.5;
this.wc5h=-22;
this.MiIm=0;
this.tYo1=function(){
image(pAzG,this.x,this.y,this.JGct,this.vJD9);
}
this.cBOK=function(){
this.xwFl+=this.mKyq;
this.MiIm+=this.xwFl;
this.MiIm*=0.9;
function jFdF(iZlN,VHDx){
if(iZlN<VHDx){
}}this.y+=this.MiIm;
if(this.y<0){
this.y=0;
this.MiIm=0;
var mKHV=mKHV+mKHV;
function ZkCL(){
}
}
}
this.mUOd=function(){
this.MiIm+=this.wc5h;
this.xwFl=0.3;
function DVdH(){
}function ZYu6(GfQy,ti9b){
}function RKB2(){
function BeNi(){
function NHoZ(){
}}}
}
this.oEb1=function(){
return(this.y>height);
}
}
function u4bE(){
this.xb95=145;
this.zsMr=random(height/2)+height/4-this.xb95/2;
function usqK(KXuJ,eB00,hZZx,AbD4){
}
this.FfnL=height-this.zsMr-this.xb95;
this.x=width;
this.JGct=40;
var Opdu=0.47487265;
function S5rD(){
}
this.K05p=400;
var jgbo=0.56248593;
function lmtj(KnkR){
}
this.LQuX=2;
var eq96=0.22477382;
function PA1O(){
}
this.tYo1=function(){
fill(255);
image(nCd8,this.x,this.zsMr-this.K05p,this.JGct,this.K05p);
image(leJh,this.x,height-this.FfnL,this.JGct,this.K05p);
function ZNHr(pYNu){
}
}
this.cBOK=function(){
this.x-=this.LQuX;
}
this.ja1U=function(){
if(NFej.x+NFej.JGct>this.x&&NFej.x<this.x+this.JGct){
if(NFej.y<this.zsMr||NFej.y+NFej.vJD9>height-this.FfnL){
return true;
}
}
return false;
var xuZI=false;
function GdTx(){
}
}
this.GIVc=function(){
return(this.x<-this.JGct);
function vXmW(){
}function DPyd(){
var w3xd=0.43914884;
function Jgk2(){
if(w3xd!==w3xd){
function CAPk(JRzu){
if(w3xd!=w3xd){
var HvCt=0.6343199;
function Dwjg(){
function j2aE(){
var zsiw=0.81245565;
function W5BD(){
}function J7qx(){
}function Xvrm(){
}if(w3xd<=w3xd){
}}}if(HvCt!=HvCt){
}var kJUI=0.18357766;
function zdSt(){
function Pf8X(){
function wO1P(){
}}}}}if(w3xd<w3xd){
var zRfj=1007411453;
function yUm1(){
}var XiEl=true;
function cXws(){
}}}}}
}
}
