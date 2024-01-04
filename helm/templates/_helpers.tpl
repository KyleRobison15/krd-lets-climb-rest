{{/*
Create chart name and version as used by the chart label.
*/}}
{{- define "krd-lets-climb-rest.chart" -}}
{{- printf "%s-%s" .Chart.Name .Chart.Version | replace "+" "_" | trunc 63 | trimSuffix "-" }}
{{- end }}

{{/*
Full Image Name
*/}}
{{- define "krd-lets-climb-rest.image" -}}
{{- $tag := .Values.pod.image.tag | default .Chart.AppVersion -}}
{{- printf "%s/%s:%s" .Values.pod.image.repository .Values.pod.image.name $tag | quote }}
{{- end }}

{{/*
Common labels
*/}}
{{- define "adp-bulk-entry-rest.labels" -}}
{{ include "adp-bulk-entry-rest.selectorLabels" . }}
edwardjones.com/systemid: OPA
edwardjones.com/app: {{ .Chart.Name }}
edwardjones.com/component: {{ .Chart.Name }}
edwardjones.com/support.language: java
{{- if .Chart.AppVersion }}
helm.sh/app-version: {{ .Chart.AppVersion | quote }}
{{- end }}
app.kubernetes.io/managed-by: {{ .Release.Service }}
{{- end }}

{{/*
Ingress Annotations
*/}}
{{- define "adp-bulk-entry-rest.ingress.annotations" -}}
nginx.ingress.kubernetes.io/ssl-redirect: "false"
{{- end }}

{{/*
Selector labels
*/}}
{{- define "krd-lets-climb-rest.selectorLabels" -}}
app: {{ .Chart.Name }}
release: {{ .Release.Name }}
{{- end }}