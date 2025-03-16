import { execSync } from "child_process";
import { readdirSync } from "fs";
import { join } from "path";

async function transpileTS() {
	await Bun.build({
		entrypoints: ["src/main/resources/ts/animation.ts"],
		outdir: "src/main/resources/public/js",
	})
}

function compileSCSS() {
	const path = "src/main/resources/scss";
	const scssDirFiles = readdirSync(path, "utf8");

	for (const file of scssDirFiles) {
		const inputFilePath = join(path, file);
		const outputFilePath = "src/main/resources/public/css";
		execSync(
			`grass -s compressed ${inputFilePath} ${outputFilePath}/${
				file.split(".")[0]
			}.css`,
		);
	}
}

transpileTS();
compileSCSS();
