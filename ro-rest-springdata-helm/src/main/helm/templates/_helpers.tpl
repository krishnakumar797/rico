{{/* vim: set filetype=mustache: */}}
{{/*
Create chart name and version as used by the chart label.
*/}}
{{- define "chart.name" -}}
{{- printf "%s-%s" .Chart.Name .Chart.Version | replace "+" "_" | trunc 63 | trimSuffix "-" -}}
{{- end -}}

{{/*
Adding image pull secret from container registry
*/}}
{{- define "imagePullSecret" -}}
{{- $usrPassword := printf "%s:%s" .Values.imageCredentials.username .Values.imageCredentials.password | b64enc -}}
{{- printf "{\"auths\": {\"%s\": {\"auth\": \"%s\"}}}" .Values.imageCredentials.registry $usrPassword | b64enc -}}
{{- end -}}

{{/*
Create a default fully qualified deployment name.
We truncate at 63 chars because some Kubernetes name fields are limited to this (by the DNS naming spec).
*/}}
{{- define "deployment.name" -}}
{{- printf "%s-%s" .Chart.Name "deployment" | trunc 63 | trimSuffix "-" -}}
{{- end -}}

{{/*
Create a default fully qualified pod name.
We truncate at 63 chars because some Kubernetes name fields are limited to this (by the DNS naming spec).
*/}}
{{- define "pod.name" -}}
{{- printf "%s-%s" .Chart.Name "pod" | trunc 63 | trimSuffix "-" -}}
{{- end -}}

{{/*
Create a default fully qualified service name.
We truncate at 63 chars because some Kubernetes name fields are limited to this (by the DNS naming spec).
*/}}
{{- define "service.name" -}}
{{- printf "%s" .Values.service.name | trunc 63 | trimSuffix "-" -}}
{{- end -}}

{{/*
Create a default fully qualified local service name for NodePort.
We truncate at 63 chars because some Kubernetes name fields are limited to this (by the DNS naming spec).
*/}}
{{- define "local.service.name" -}}
{{- printf "%s-%s" "local" .Values.service.name | trunc 63 | trimSuffix "-" -}}
{{- end -}}

{{/*
Create a default fully qualified local debug service name for NodePort.
We truncate at 63 chars because some Kubernetes name fields are limited to this (by the DNS naming spec).
*/}}
{{- define "local.debug.service.name" -}}
{{- printf "%s-%s-%s" "local" "debug" .Values.service.name | trunc 63 | trimSuffix "-" -}}
{{- end -}}

{{/*
Create a default fully qualified service monitor name.
We truncate at 63 chars because some Kubernetes name fields are limited to this (by the DNS naming spec).
*/}}
{{- define "service.monitor" -}}
{{- printf "%s-%s" .Values.service.name "monitor" | trunc 63 | trimSuffix "-" -}}
{{- end -}}

{{/*
Create a default fully qualified target port name.
We truncate at 63 chars because some Kubernetes name fields are limited to this (by the DNS naming spec).
*/}}
{{- define "target.port.name" -}}
{{- printf "%s" .Values.target.portName | trunc 15 | trimSuffix "-" -}}
{{- end -}}

{{/*
Create a default fully qualified service cassandra name.
We truncate at 63 chars because some Kubernetes name fields are limited to this (by the DNS naming spec).
*/}}
{{- define "db.service" -}}
{{- printf "%s" .Values.database.service.name | trunc 63 | trimSuffix "-" -}}
{{- end -}}
