const javaFile = Java.type('java.io.File');
const javaFiles = Java.type('java.nio.file.Files');
const javaPaths = Java.type('java.nio.file.Paths');
const javaStandardCopyOption = Java.type('java.nio.file.StandardCopyOption');

// Définir les répertoires source et destination
const appData = Java.type('java.lang.System').getenv('APPDATA');
const sourceDir = javaPaths.get(appData, '.Apocalyptic Survival Launcher', 'mods');
const destDir = javaPaths.get(appData, '.Apocalyptic Survival Launcher', 'resourcepacks');

// Vérifier si les répertoires source et destination existent
if (!javaFiles.exists(sourceDir)) {
    throw new Error(`Le répertoire source n'existe pas : ${sourceDir}`);
}
if (!javaFiles.exists(destDir)) {
    javaFiles.createDirectories(destDir);
    console.log(`Le répertoire de destination a été créé : ${destDir}`);
}

// Lister les fichiers dans le répertoire source
const directoryStream = javaFiles.newDirectoryStream(sourceDir, '*.zip');
for (const entry of directoryStream) {
    const sourceFile = entry.toAbsolutePath();
    const destFile = destDir.resolve(entry.getFileName());

    // Copier le fichier
    try {
        javaFiles.copy(sourceFile, destFile, javaStandardCopyOption.REPLACE_EXISTING);
        console.log(`${entry.getFileName()} a été copié dans ressourcepacks.`);
    } catch (err) {
        console.error(`Erreur lors de la copie de ${entry.getFileName()}: ${err.message}`);
    }
}