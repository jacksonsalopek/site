import { execSync } from "child_process";
import { readFileSync, readdirSync, writeFileSync } from "fs";
import { join } from "path";

function transpileTS() {
	const transpiler = new Bun.Transpiler({
		loader: "ts",
		target: "browser",
	});

	const path = "src/main/resources/ts";
	const tsDirFiles = readdirSync(path);

	for (const file of tsDirFiles) {
		const filePath = join(path, file);
		const fileContent = readFileSync(filePath, "utf8");
		const transpiledContents = transpiler.transformSync(fileContent);
		writeFileSync(
			`src/main/resources/public/js/${file.split(".")[0]}.js`,
			transpiledContents,
		);
	}
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
