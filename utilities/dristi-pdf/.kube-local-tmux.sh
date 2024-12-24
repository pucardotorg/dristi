#!/bin/bash

SESSION_NAME="portforward-session"
COMMANDS=(
    "kubectl port-forward svc/case -n egov 8091:8080"
    "kubectl port-forward svc/egov-mdms-service -n egov 8081:8080"
    "kubectl port-forward svc/egov-filestore -n egov 8084:8080"
    "kubectl port-forward svc/pdf-service -n egov 8070:8080"
)

tmux new-session -d -s $SESSION_NAME

for i in "${!COMMANDS[@]}"; do
    if [ $i -eq 0 ]; then
        # First command goes in the first window
        tmux send-keys -t $SESSION_NAME "${COMMANDS[$i]}" C-m
    else
        # Subsequent commands go in new windows
        tmux new-window -t $SESSION_NAME
        tmux send-keys -t $SESSION_NAME:$i "${COMMANDS[$i]}" C-m
    fi
done

tmux attach-session -t $SESSION_NAME

