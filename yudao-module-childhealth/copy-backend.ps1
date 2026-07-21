$ErrorActionPreference = 'Stop'
$source = 'd:\Backup\Documents\Downloads\childhealth\childhealth\chinderhealth'
$dest = 'd:\childhealth-split\childhealth-backend'

if (-not (Test-Path $dest)) {
    New-Item -ItemType Directory -Path $dest -Force | Out-Null
}

$dirsToCopy = @(
    'yudao-dependencies',
    'yudao-framework',
    'yudao-module-system',
    'yudao-module-infra',
    'yudao-module-childhealth',
    'yudao-server'
)

$copyErrors = @()

foreach ($dir in $dirsToCopy) {
    $srcPath = Join-Path $source $dir
    $dstPath = Join-Path $dest $dir
    if (Test-Path $srcPath) {
        Write-Host "[COPY] $dir -> $dstPath"
        try {
            Copy-Item -Path $srcPath -Destination $dstPath -Recurse -Force
            Write-Host "  [OK] Copied $dir"
        } catch {
            Write-Host "  [ERROR] Failed to copy ${dir}: $($_.Exception.Message)"
            $copyErrors += "$dir : $($_.Exception.Message)"
        }
    } else {
        Write-Host "[WARN] Source directory not found: $srcPath"
        $copyErrors += "Missing source: $srcPath"
    }
}

$rootFiles = @('pom.xml', 'lombok.config', 'README.md', 'start-backend.bat')
foreach ($file in $rootFiles) {
    $srcFile = Join-Path $source $file
    $dstFile = Join-Path $dest $file
    if (Test-Path $srcFile) {
        try {
            Copy-Item -Path $srcFile -Destination $dstFile -Force
            Write-Host "[COPY-FILE] $file"
        } catch {
            Write-Host "[ERROR] Failed to copy ${file}: $($_.Exception.Message)"
            $copyErrors += "$file : $($_.Exception.Message)"
        }
    } else {
        Write-Host "[WARN] Root file not found: $srcFile"
        $copyErrors += "Missing root file: $srcFile"
    }
}

Write-Host ""
Write-Host "=== Cleanup target/ .idea/ *.iml from destination ==="
$targetDirs = Get-ChildItem -Path $dest -Recurse -Directory -ErrorAction SilentlyContinue | Where-Object { $_.Name -eq 'target' }
foreach ($td in $targetDirs) {
    Write-Host "  [REMOVE-DIR] $($td.FullName)"
    Remove-Item -Path $td.FullName -Recurse -Force -ErrorAction SilentlyContinue
}
$ideaDirs = Get-ChildItem -Path $dest -Recurse -Directory -ErrorAction SilentlyContinue | Where-Object { $_.Name -eq '.idea' }
foreach ($id in $ideaDirs) {
    Write-Host "  [REMOVE-DIR] $($id.FullName)"
    Remove-Item -Path $id.FullName -Recurse -Force -ErrorAction SilentlyContinue
}
$imlFiles = Get-ChildItem -Path $dest -Recurse -File -Filter '*.iml' -ErrorAction SilentlyContinue
foreach ($iml in $imlFiles) {
    Write-Host "  [REMOVE-FILE] $($iml.FullName)"
    Remove-Item -Path $iml.FullName -Force -ErrorAction SilentlyContinue
}

Write-Host ""
Write-Host "=== Summary ==="
Write-Host "Errors count: $($copyErrors.Count)"
if ($copyErrors.Count -gt 0) {
    $copyErrors | ForEach-Object { Write-Host "  - $_" }
}
Write-Host "Done."
