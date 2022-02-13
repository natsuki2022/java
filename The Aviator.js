function init(event){
    createScene();

    createLights();

    createPlane();

    createSea();

    createSky();

    document.addEventListener('mousemove', handleMouseMove, false);

    loop();//循环函数，用于最后每一个帧的重绘，实现动画效果
}
//通过dat.gui来调整环境光
var controls = new function () {//声明一个控制对象
    this.ambientLightColor = "#dc8874";
}
//环境光的值可以是16进制的数值，如"#ffffff"，每次通过gui调整了color值都会触发下面的匿名函数从而调整环境光的颜色，环境光加入到场景中后每次渲染场景时都会使用最新的环境光颜色值，从而实现了使用gui调整环境光颜色的功能
var gui = new dat.GUI;//创建gui对象
gui.addColor(controls,'ambientLightColor').onChange(function (e) {
	ambientLight.color = new THREE.Color(e);//
});
function createScene(){

    HEIGHT = window.innerHeight;
    WIDTH = window.innerWidth;

    scene = new THREE.Scene();//创建场景
    scene.fog = new THREE.Fog(0xf7d9aa, 100,950);//使用雾化的效果
	var axes = new THREE.AxisHelper(200);//场景中添加一个三维坐标系，便于观察图形的位置
    scene.add(axes);
    aspectRatio = WIDTH / HEIGHT;//宽高比设置为窗口大小，避免图案的变形
    fieldOfView = 50;
    nearPlane = 0.1;
    farPlane = 10000;
    camera = new THREE.PerspectiveCamera(fieldOfView,aspectRatio,nearPlane,farPlane);//使用一个透视相机使物体具有3d的效果
    camera.position.x = 0;//相机的位置和视点将影响观察到的物体
    camera.position.z = 200;
    camera.position.y = 100;//待优化

    renderer = new THREE.WebGLRenderer({ alpha: true, antialias: true });//声明一个webgl的渲染器，这个渲染器就如同html中的canvas
    renderer.setSize(WIDTH,HEIGHT);
    renderer.shadowMap.enabled = true;
    container = document.getElementById('world');
    container.appendChild(renderer.domElement);//将这个渲染器加到html当中


    function createLights(){
        hemisphereLight = new THREE.HemisphereLight(0xbbbbbb,0x000000, .9);
        ambientLight = new THREE.AmbientLight(controls.ambientLightColor);
        shadowLight = new THREE.DirectionalLight(0xffffff, .9);
        shadowLight.castShadow = true;
        shadowLight.shadow.camera.left = -400;
        shadowLight.shadow.camera.right = 400;
        shadowLight.shadow.camera.top = 400;
        shadowLight.shadow.camera.bottom = -400;
        shadowLight.shadow.camera.near = 1;
        shadowLight.shadow.camera.far = 1000;
        shadowLight.shadow.mapSize.width = 2048;
        shadowLight.shadow.mapSize.height = 2048;
    //每次设置完灯光都需要把他添加到场景中
        scene.add(hemisphereLight);
        scene.add(shadowLight);
        scene.add(ambientLight);
    }
    Sea = function(){
        var geom = new THREE.CylinderGeometry(600,600,800,40,10);
        geom.applyMatrix(new THREE.Matrix4().makeRotationX(-Math.PI/2));
        var mat = new THREE.MeshPhongMaterial({
            color:Colors.blue,
            transparent:true,
            opacity:.6,
            shading:THREE.FlatShading,
        });
        this.mesh = new THREE.Mesh(geom, mat);
        this.mesh.receiveShadow = true;

    }

    var sea;
    function createSea(){
        sea = new Sea();
        sea.mesh.position.y = -600;
        scene.add(sea.mesh);
    }
    //构造一个云朵对象
    Cloud = function(){
        this.mesh = new THREE.Object3D();
        var geom = new THREE.BoxGeometry(20,20,20);

        var mat = new THREE.MeshPhongMaterial({
            color:Colors.white,
        });
        var nBlocs = 3+Math.floor(Math.random()*3);

        for(i=0;i<nBlocs;i++){
        //实现位置随机，大小随机
            var m = new THREE.Mesh(geom, mat);
            m.position.x = i*15;
            m.position.y =Math.random()*10;
            m.position.z = Math.random()*10;
            m.rotation.z = Math.random()*Math.PI*2;
            m.rotation.y = Math.random()*Math.PI*2;
            var s = .1 + Math.random()*.9;
            m.scale.set(s,s,s);

            m.castShadow = true;
            m.receiveShadow = true;

            this.mesh.add(m);
        }
    }

    Sky = function(){
        this.mesh = new THREE.Object3D();
        this.nClouds = 20;
        var stepAngle = Math.PI*2 / this.nClouds;
        for (var i=0;i<this.nClouds;i++){
            var c = new Cloud();
            var a = stepAngle*i;
            var h = 750 + Math.random()*200;
            c.mesh.position.y = Math.sin(a)*h;
            c.mesh.position.x = Math.cos(a)*h;
            c.mesh.rotation.z = - Math.PI/2+a;
            c.mesh.position.z = -50-Math.random()*400;
            var s = 1+Math.random()*2;
            c.mesh.scale.set(s,s,s);
            this.mesh.add(c.mesh);
        }
    }
    var sky;

    function createSky(){
        sky = new Sky();
        sky.mesh.position.y = -600;
        scene.add(sky.mesh);
    }
    var AirPlane = function() {

        this.mesh = new THREE.Object3D();

        // 这里要做的是一个驾驶舱
        var geomCockpit = new THREE.BoxGeometry(80,50,50,1,1,1);
        var matCockpit = new THREE.MeshPhongMaterial({color:Colors.red, shading:THREE.FlatShading});
        geomCockpit.vertices[4].y-=10;
        geomCockpit.vertices[4].z+=20;
        geomCockpit.vertices[5].y-=10;
        geomCockpit.vertices[5].z-=20;
        geomCockpit.vertices[6].y+=20;
        geomCockpit.vertices[6].z+=20;
        geomCockpit.vertices[7].y+=20;
        geomCockpit.vertices[7].z-=20;

        var cockpit = new THREE.Mesh(geomCockpit, matCockpit);
        cockpit.castShadow = true;
        cockpit.receiveShadow = true;
        this.mesh.add(cockpit);

        // 还要有引擎盖
        var geomEngine = new THREE.BoxGeometry(20,50,50,1,1,1);
        var matEngine = new THREE.MeshPhongMaterial({color:Colors.white, shading:THREE.FlatShading});
        var engine = new THREE.Mesh(geomEngine, matEngine);
        engine.position.x = 40;
        engine.castShadow = true;
        engine.receiveShadow = true;
        this.mesh.add(engine);

        // 做个尾巴吧
        var geomTailPlane = new THREE.BoxGeometry(15,20,5,1,1,1);
        var matTailPlane = new THREE.MeshPhongMaterial({color:Colors.red, shading:THREE.FlatShading});
        var tailPlane = new THREE.Mesh(geomTailPlane, matTailPlane);
        tailPlane.position.set(-35,25,0);
        tailPlane.castShadow = true;
        tailPlane.receiveShadow = true;
        this.mesh.add(tailPlane);

        // 机翼当然少不了，用长长的矩形穿过机身，多么美妙！
        var geomSideWing = new THREE.BoxGeometry(40,8,150,1,1,1);
        var matSideWing = new THREE.MeshPhongMaterial({color:Colors.red, shading:THREE.FlatShading});
        var sideWing = new THREE.Mesh(geomSideWing, matSideWing);
        sideWing.castShadow = true;
        sideWing.receiveShadow = true;
        this.mesh.add(sideWing);

        // 飞机前端旋转的螺旋桨
        var geomPropeller = new THREE.BoxGeometry(20,10,10,1,1,1);
        var matPropeller = new THREE.MeshPhongMaterial({color:Colors.brown, shading:THREE.FlatShading});
        this.propeller = new THREE.Mesh(geomPropeller, matPropeller);
        this.propeller.castShadow = true;
        this.propeller.receiveShadow = true;

        // 螺旋桨
        var geomBlade = new THREE.BoxGeometry(1,100,20,1,1,1);
        var matBlade = new THREE.MeshPhongMaterial({color:Colors.brownDark, shading:THREE.FlatShading});

        var blade = new THREE.Mesh(geomBlade, matBlade);
        blade.position.set(8,0,0);
        blade.castShadow = true;
        blade.receiveShadow = true;
        this.propeller.add(blade);
        this.propeller.position.set(50,0,0);
        this.mesh.add(this.propeller);

    };

    var airplane;

    function createPlane(){
        airplane = new AirPlane();
        airplane.mesh.scale.set(.25,.25,.25);
        airplane.mesh.position.y = 100;
        scene.add(airplane.mesh);
    }

    function handleMouseMove(event) {

        // 我们要把鼠标的坐标值转换成webgl系统中规格化的数值，从-1到1
        // 这种转换很简单的伙计！tx = (x-width/2)/(width/2)

        var tx = -1 + (event.clientX / WIDTH)*2;

        // y轴在窗口坐标系和webg坐标系的方向是相反的，因此我们把他逆一下就可以


        var ty = 1 - (event.clientY / HEIGHT)*2;
        mousePos = {x:tx, y:ty};

    }


    function updatePlane(){
        var targetY = 100+mousePos.y*75;//控制飞机在y轴25到175的位置
        var targetX = mousePos.x*195;//控制飞机在x轴-195到195的位置

        // 每一帧移动飞机移动的距离，使飞机最终到达鼠标的位置，这样制造出飞机缓缓飞向指定位置的效果，而不会显得很突兀。
        airplane.mesh.position.y += (targetY-airplane.mesh.position.y)*0.1;
        airplane.mesh.position.x += (targetX-airplane.mesh.position.x)*0.5;
        // 通过剩余距离的长度来计算旋转地幅度，这样飞机如果一次性移动的距离很多相应的旋转幅度就越大，与真实的情况也符合，使动画更加真实。
        airplane.mesh.rotation.z = (targetY-airplane.mesh.position.y)*0.0256;
        airplane.mesh.rotation.x = (airplane.mesh.position.y-targetY)*0.0256;

        airplane.propeller.rotation.x += 0.3;

    }
    function loop(){
        airplane.propeller.rotation.x += 0.3;
        sea.mesh.rotation.z += .005;
        sky.mesh.rotation.z += .01;
        updatePlane();
        airplane.pilot.updateHairs();


        // 渲染
        renderer.render(scene, camera);

        // 再次调用
        requestAnimationFrame(loop);
    }
