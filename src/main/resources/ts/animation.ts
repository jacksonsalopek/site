import {
	BufferGeometry,
	Clock,
	Color,
	DoubleSide,
	Mesh,
	MeshLambertMaterial,
	PerspectiveCamera,
	PointLight,
	Scene,
	WebGLRenderer,
} from "three";
import { OrbitControls } from "three/addons/controls/OrbitControls.js";
import { AsciiEffect } from "three/addons/effects/AsciiEffect.js";
import { STLLoader } from "three/addons/loaders/STLLoader.js";

export const animationState: any & {
	camera: PerspectiveCamera;
	controls: OrbitControls | undefined;
	domElement: unknown | undefined;
	effect: AsciiEffect | undefined;
} = {
	camera: new PerspectiveCamera(45, window.innerWidth / window.innerHeight),
	clock: new Clock(),
	controls: undefined,
	domElement: undefined,
	effect: undefined,
	lights: [new PointLight(0xffffff, 1), new PointLight(0xffffff, 0.5)],
	material: new MeshLambertMaterial({
		flatShading: true,
		side: DoubleSide,
	}),
	mesh: new Mesh(),
	renderer: new WebGLRenderer(),
	rotate: true,
	scene: new Scene(),
	stlLoader: new STLLoader(),
	config: {
		color: "#fff",
		backgroundColor: "#000",
		charSet: " .:-+*=%@#",
		resolution: 0.205,
		modelUrl: "/public/stl/porsche.stl",
		oscillate: false,
		amplitude: 1 / 3,
	},
};

export function createEffect(): AsciiEffect {
	if (!animationState.renderer)
		throw new Error("WebGLRenderer not instantiated!");
	const effect = new AsciiEffect(
		animationState.renderer,
		animationState.config.charSet,
		{
			invert: true,
			resolution: animationState.config.resolution,
		},
	);
	const width =
		window.innerWidth - (window.innerWidth * 80) / window.innerHeight;
	const height = window.innerHeight - 80;
	effect.setSize(width * 0.5, height * 0.5);
	effect.domElement.style.color = animationState.config.color;
	effect.domElement.style.backgroundColor =
		animationState.config.backgroundColor;
	return effect;
}

export function render(effect: AsciiEffect) {
	effect.render(animationState.scene, animationState.camera);
	window.requestAnimationFrame(tick);
}

export function toggleRotation() {
	animationState.rotate = !animationState.rotate;
}

export function tick() {
	if (animationState.effect) {
		if (animationState.rotate) {
			animationState.mesh.rotation.z =
				animationState.clock.getElapsedTime() / 3;
			if (animationState.config.oscillate)
				animationState.mesh.rotation.y =
					animationState.config.amplitude *
					Math.sin(animationState.clock.getElapsedTime() / 3);
			render(animationState.effect);
		} else {
			render(animationState.effect);
		}
	}
}

export function adjustCameraZoom() {
	if (
		/Android|webOS|iPhone|iPad|iPod|BlackBerry|IEMobile|Opera Mini/i.test(
			navigator.userAgent,
		)
	) {
		animationState.camera.zoom = 1;
		animationState.camera.updateProjectionMatrix();
		return true;
	}
	if (window.innerWidth < 500) {
		animationState.camera.zoom = 1;
		animationState.camera.updateProjectionMatrix();
		return true;
	}
	if (window.innerWidth < 600) {
		animationState.camera.zoom = 1;
		animationState.camera.updateProjectionMatrix();
		return true;
	}
	if (window.innerWidth < 800) {
		animationState.camera.zoom = 1;
		animationState.camera.updateProjectionMatrix();
		return true;
	}
	if (window.innerWidth < 1500) {
		animationState.camera.zoom = 1.2;
		animationState.camera.updateProjectionMatrix();
		return true;
	}
	animationState.camera.zoom = 1;
	animationState.camera.updateProjectionMatrix();
	return true;
}

export function onLoad(geometry: BufferGeometry) {
	animationState.mesh.material = animationState.material;
	animationState.mesh.geometry = geometry;

	geometry.computeVertexNormals();

	animationState.mesh.geometry.center();
	animationState.mesh.rotation.x = (-90 * Math.PI) / 180;
	animationState.mesh.geometry.computeBoundingBox();

	const boundingBox = animationState.mesh.geometry.boundingBox;
	if (boundingBox) {
		animationState.mesh.position.y =
			(boundingBox.max.z - boundingBox.min.z) / 5;
		animationState.camera.position.x = boundingBox.max.x * 4;
		animationState.camera.position.y = boundingBox.max.y;
		animationState.camera.position.z = boundingBox.max.z * 3;
		adjustCameraZoom();
	}

	animationState.scene.add(animationState.mesh);
	animationState.controls = new OrbitControls(
		animationState.camera,
		animationState.domElement,
	);
	animationState.controls.enableZoom = true;

	const animationContainer = document.getElementById("animation");
	animationContainer?.removeChild(animationContainer?.children[0]);

	tick();
}

export function onResize() {
	if (!animationState.renderer)
		throw new Error("WebGLRenderer not instantiated!");
	animationState.camera.aspect = window.innerWidth / window.innerHeight;
	if (!adjustCameraZoom()) {
		animationState.camera.updateProjectionMatrix();
	}
	const width =
		window.innerWidth - (window.innerWidth * 80) / window.innerHeight;
	const height = window.innerHeight - 80;

	animationState.renderer.setSize(width * 0.5, height * 0.5);
	animationState.effect?.setSize(width * 0.5, height * 0.5);
}

export function bootstrap() {
	animationState.lights[0].position.set(100, 100, 400);
	animationState.lights[1].position.set(-500, 100, -400);
	animationState.scene.add(...animationState.lights);
	animationState.scene.background = new Color(
		animationState.config.backgroundColor,
	);
	animationState.effect = createEffect();
	animationState.stlLoader.load(animationState.config.modelUrl, onLoad);
	return animationState.effect.domElement;
}

window.onload = () => {
	animationState.domElement = bootstrap();
	animationState.domElement.style.background = "transparent";
	const animationContainer = document.getElementById("animation");
	animationContainer?.appendChild(animationState.domElement);
};

window.onresize = onResize;
