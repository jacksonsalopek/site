import {
	WebGLRenderer,
	Scene,
	PerspectiveCamera,
	Color,
	DirectionalLight,
	Clock,
	Object3D
} from "three";
import { GLTFLoader } from "three/addons/loaders/GLTFLoader.js";
import { ASCII } from "./ascii";
import { EffectComposer, EffectPass, RenderPass } from "postprocessing";
import { OrbitControls } from "three/examples/jsm/controls/OrbitControls.js";

export class App {
	private container: HTMLElement;
	private canvas: { width: number; height: number };
	private isMobile: boolean;
	private config: { color: string; backgroundColor: string; charSet: string; modelUrl: string };
	private renderer?: WebGLRenderer;
	private scene?: Scene;
	private camera: PerspectiveCamera;
	private light?: DirectionalLight;
	private gltfLoader?: GLTFLoader;
	private composer?: EffectComposer;
	private controls?: OrbitControls;
	private clock: Clock;
	private mesh?: Object3D;

	constructor() {
		this.container = document.getElementById("animation") as HTMLElement;
		this.canvas = {
			width: this.container.clientWidth,
			height: this.container.clientHeight
		};

		this.camera = new PerspectiveCamera(
			80,
			this.canvas.width / this.canvas.height,
			0.1,
			1000
		);
		this.camera.position.x = 2
		this.camera.position.y = 2.36
		this.camera.position.z = -2.68

		this.clock = new Clock();
		this.isMobile = matchMedia("(pointer: coarse)").matches;
		this.config = {
			color: "#fff",
			backgroundColor: "#000",
			charSet: " .:-+*=%@#",
			modelUrl: "/public/glb/lambo.glb"
		};

		this.initThree();
		this.loadModel();
		this.initAsciiEffect();
		if (this.renderer) this.container.appendChild(this.renderer.domElement);
	}

	initThree() {
		const pixelRatio = window.devicePixelRatio || 1;
		let antialias = true;
		if (this.isMobile) antialias = false;
		if (pixelRatio > 2) antialias = false;

		// Create renderer
		this.renderer = new WebGLRenderer({
			powerPreference: 'high-performance',
			alpha: true,
			antialias: antialias,
			stencil: false
		});
		this.renderer.setSize(this.canvas.width, this.canvas.height);

		// Create scene and camera
		this.scene = new Scene();
		this.scene.background = new Color(this.config.backgroundColor);

		// Add lights
		this.light = new DirectionalLight("#fff", 6.5);
		this.light.position.set(50, 50, 50);
		this.scene.add(this.light);

		// Set up resize listener
		window.addEventListener('resize', this.onResize.bind(this));
	}

	loadModel() {
		this.gltfLoader = new GLTFLoader();
		this.gltfLoader.load(this.config.modelUrl, (res) => {
			// Create mesh
			this.scene?.add(res.scene);
			this.mesh = res.scene.getObjectByName("Sketchfab_model");
			if (!this.mesh) return;
			this.mesh.castShadow = false;
			this.mesh.receiveShadow = false;

			this.container?.removeChild(this.container?.children[0]);

			// Start animation once model is loaded
			this.animate();
		});
	}

	initAsciiEffect() {
		this.controls = new OrbitControls(this.camera, this.renderer?.domElement);
		this.controls.enableZoom = true;
		this.controls.addEventListener("change", () => {
			// This fires whenever any control change happens (pan, rotate, zoom)
			const distance = this.camera.position.distanceTo(this.controls!.target);
			
			// Log both the distance and a normalized zoom factor
			console.log({
			distance: distance.toFixed(2),
			zoomFactor: this.camera.zoom.toFixed(2),
			position: this.camera.position
			}); 
		});
		const asciiEffect = new ASCII({
			fontSize: 35,
			cellSize: 16,
			invert: false,
			color: this.config.color,
			characters: this.config.charSet
		});

		this.composer = new EffectComposer(this.renderer);
		this.composer.addPass(new RenderPass(this.scene, this.camera));
		this.composer.addPass(new EffectPass(this.camera, asciiEffect));
	}

	animate() {
		requestAnimationFrame(this.animate.bind(this));
		if (this.mesh) {
			this.mesh.rotation.z = this.clock.getElapsedTime() / 3;
		}
		this.composer?.render();
	}

	onResize() {
		if (!this.container) return;

		this.canvas = {
			width: this.container.clientWidth,
			height: this.container.clientHeight
		};

		this.camera.aspect = this.canvas.width / this.canvas.height;
		this.camera.updateProjectionMatrix();

		this.renderer?.setSize(this.canvas.width, this.canvas.height);
		this.composer?.setSize(this.canvas.width, this.canvas.height);
	}
}

// Initialize the application when window loads
window.onload = () => {
	new App();
};