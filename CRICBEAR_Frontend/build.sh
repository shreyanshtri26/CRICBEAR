#!/usr/bin/env bash
# Builds both dashboards into one static site: public at /, admin/coach at /admin.
set -euo pipefail
cd "$(dirname "$0")"
rm -rf dist
(cd dashboardforpublicuser && CI=false npm run build)
(cd dashboardforadmincoach && PUBLIC_URL=/admin CI=false npm run build)
mkdir -p dist/admin
cp -r dashboardforpublicuser/build/. dist/
cp -r dashboardforadmincoach/build/. dist/admin/
