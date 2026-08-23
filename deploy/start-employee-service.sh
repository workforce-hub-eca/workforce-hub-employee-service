#!/usr/bin/env bash
set -euo pipefail

if [ -z "${GCP_PROJECT_ID:-}" ]; then
  echo "Error: GCP_PROJECT_ID is not set." >&2
  exit 1
fi

if [ -z "${EMPLOYEE_DB_SECRET_ID:-}" ]; then
  echo "Error: EMPLOYEE_DB_SECRET_ID is not set." >&2
  exit 1
fi

employee_db_password="$(
  /snap/bin/gcloud secrets versions access latest \
    --secret="${EMPLOYEE_DB_SECRET_ID}" \
    --project="${GCP_PROJECT_ID}"
)"

if [ -z "${employee_db_password}" ]; then
  echo "Error: Retrieved Employee database secret is empty." >&2
  exit 1
fi

export EMPLOYEE_DB_PASSWORD="${employee_db_password}"
unset employee_db_password

exec /usr/bin/java \
  -jar \
  /opt/workforce-hub/apps/employee-service-0.0.1-SNAPSHOT.jar
