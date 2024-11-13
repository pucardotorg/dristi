import React from "react";
import { useEffect, useState, useMemo, useRef } from "react";

const TranscriptComponent = ({ setTranscriptText, isRecording, setIsRecording, activeTab, setWitnessDepositionText }) => {
  const [context, setContext] = useState(null);
  const [globalStream, setGlobalStream] = useState(null);
  const [processor, setProcessor] = useState(null);
  const endTimeRef = useRef([0, 0, 0]);
  const [transcription, setTranscription] = useState("");
  const [webSocketStatus, setWebSocketStatus] = useState("Not Connected");
  const [transcriptionUrl, setTranscriptionUrl] = useState("");
  const [sendOriginal, setSendOriginal] = useState("");
  const [clientId, setClientId] = useState(null);
  const [currentPosition, setCurrentPosition] = useState(0);
  const [selectedLanguage, setSelectedLanguage] = useState("english");
  const [selectedAsrModel, setSelectedAsrModel] = useState("bhashini");
  const [websocket, setWebsocket] = useState(null);
  const inputSourceRef = useRef("mic");
  const [roomId, setRoomId] = useState(null);
  const [audioUrl, setAudioUrl] = useState("");
  const [isConnected, setIsConnected] = useState(false);

  const bufferSize = 4096;

  useEffect(() => {
    initWebSocket();
  }, []);

  const joinRoom = () => {
    console.log(websocket, "websocket join room", WebSocket.OPEN);
    if (websocket && websocket.readyState === WebSocket.OPEN) {
      const message = {
        type: "joined_room",
        room_id: roomId,
      };
      console.log(websocket, message, "websocket join room success");

      websocket.send(JSON.stringify(message));
    }
  };

  const createRoom = () => {
    console.log(websocket, "websocket create room");
    if (websocket && websocket.readyState === WebSocket.OPEN) {
      const message = {
        type: "create_room",
        room_id: roomId,
      };
      websocket.send(JSON.stringify(message));
    }
  };

  const initWebSocket = () => {
    const websocketAddress = "wss://dristi-kerala-dev.pucar.org/transcription";

    if (!websocketAddress) {
      console.log("WebSocket address is required.");
      return;
    }

    const ws = new WebSocket(websocketAddress);

    ws.onopen = () => {
      console.log("WebSocket connection established");
      setWebSocketStatus("Connected");
    };

    ws.onclose = (event) => {
      console.log("WebSocket connection closed", event);
      setWebSocketStatus("Not Connected");
    };

    ws.onmessage = (event) => {
      const data = JSON.parse(event.data);
      if (data.type === "joined_room" || data.type === "refresh_transcription") {
        handleRoomJoined(data);
      } else {
        updateTranscription(data);
      }
    };

    setWebsocket(ws);
  };

  const handleRoomJoined = (data) => {
    setClientId(data.client_id);
    setRoomId(data.room_id);
    setTranscriptionUrl(data.transcript_url);
    setAudioUrl(data.audio_url);
  };

  const updateTranscription = (transcriptData) => {
    if (transcriptData.words && transcriptData.words.length > 0) {
      const newTranscription = transcriptData.words
        .map((wordData) => {
          const probability = wordData.probability;
          let color = "black";
          if (probability > 0.9) color = "green";
          else if (probability > 0.6) color = "orange";
          else color = "red";
          return `<span style="color: ${color}">${wordData.word} </span>`;
        })
        .join("");
      setTranscription((prev) => prev + " " + newTranscription);
    } else {
      setTranscription((prev) => prev + " " + transcriptData.text);
    }
    activeTab === "Witness Deposition"
      ? setWitnessDepositionText((prev) => prev + " " + transcriptData.text)
      : setTranscriptText((prev) => prev + " " + transcriptData.text);
    setSendOriginal((prev) => prev + " " + transcriptData.text);
  };
  const startRecording = () => {
    if (isRecording) {
      return;
    }
    setIsRecording(true);

    const inputSource = inputSourceRef.current.value;
    if (inputSource === "mic") {
      startMicRecording();
    } else {
      window.alert("mic is not connected!");
    }
  };

  const stopRecording = () => {
    if (!isRecording) return;

    setIsRecording(false);

    if (globalStream) {
      globalStream.getTracks().forEach((track) => track.stop());
      setGlobalStream(null);
    }
    if (processor) {
      setCurrentPosition(context.currentTime);
      processor.disconnect();
      setProcessor(null);
    }
    if (context) {
      context.close().then(() => setContext(null));
    }
    const now = new Date();
    endTimeRef.current = [now.getHours(), now.getMinutes(), now.getSeconds()];
  };

  const processAudio = (e, audioContext) => {
    if (!audioContext) {
      console.error("Audio context is not initialized");
      return;
    }
    const inputSampleRate = audioContext.sampleRate;
    const outputSampleRate = 16000;

    const left = e.inputBuffer.getChannelData(0);
    const downsampledBuffer = downsampleBuffer(left, inputSampleRate, outputSampleRate);
    const audioData = convertFloat32ToInt16(downsampledBuffer);
    if (websocket && websocket.readyState === WebSocket.OPEN) {
      const audioBase64 = bufferToBase64(audioData);
      const message = {
        type: "audio",
        data: audioBase64,
        room_id: roomId,
        client_id: clientId,
      };
      websocket.send(JSON.stringify(message));
    }
  };

  const downsampleBuffer = (buffer, inputSampleRate, outputSampleRate) => {
    if (inputSampleRate === outputSampleRate) {
      return buffer;
    }
    const sampleRateRatio = inputSampleRate / outputSampleRate;
    const newLength = Math.round(buffer.length / sampleRateRatio);
    const result = new Float32Array(newLength);
    let offsetResult = 0;
    let offsetBuffer = 0;
    while (offsetResult < result.length) {
      const nextOffsetBuffer = Math.round((offsetResult + 1) * sampleRateRatio);
      let accum = 0,
        count = 0;
      for (let i = offsetBuffer; i < nextOffsetBuffer && i < buffer.length; i++) {
        accum += buffer[i];
        count++;
      }
      result[offsetResult] = accum / count;
      offsetResult++;
      offsetBuffer = nextOffsetBuffer;
    }
    return result;
  };

  const convertFloat32ToInt16 = (buffer) => {
    const l = buffer.length;
    const buf = new Int16Array(l);
    for (let i = 0; i < l; i++) {
      buf[i] = Math.min(1, buffer[i]) * 0x7fff;
    }
    return buf.buffer;
  };

  const bufferToBase64 = (buffer) => {
    const binary = String.fromCharCode.apply(null, new Uint8Array(buffer));
    return window.btoa(binary);
  };

  const startMicRecording = () => {
    const AudioContext = window.AudioContext || window.webkitAudioContext;
    const newContext = new AudioContext();
    setContext(newContext);

    sendAudioConfig(newContext);
    navigator.mediaDevices
      .getUserMedia({ audio: true })
      .then((stream) => {
        setGlobalStream(stream);
        const input = newContext.createMediaStreamSource(stream);
        const newProcessor = newContext.createScriptProcessor(bufferSize, 1, 1);
        newProcessor.onaudioprocess = (e) => processAudio(e, newContext);
        input.connect(newProcessor);
        newProcessor.connect(newContext.destination);
        setProcessor(newProcessor);
      })
      .catch((error) => console.error("Error accessing microphone", error));
  };

  useEffect(() => {
    if (webSocketStatus === "Connected") {
      createRoom();
      setIsConnected(true);
    }
  }, [webSocketStatus]);

  const sendAudioConfig = (context) => {
    if (!context) {
      console.error("Audio context is not initialized");
      return;
    }
    const audioConfig = {
      type: "config",
      room_id: roomId,
      client_id: clientId,
      data: {
        sampleRate: context.sampleRate,
        bufferSize: bufferSize,
        channels: 1,
        language: selectedLanguage !== "multilingual" ? selectedLanguage : null,
        asr_model: selectedAsrModel,
        processing_strategy: "silence_at_end_of_chunk",
        processing_args: {
          chunk_length_seconds: 1,
          chunk_offset_seconds: 0.1,
        },
      },
    };

    websocket.send(JSON.stringify(audioConfig));
  };

  return (
    <div>
      <input type="radio" id="micInput" name="inputSource" value="mic" defaultChecked ref={inputSourceRef} style={{ display: "none" }} />

      {!isConnected && (
        <div style={{ textAlign: "right" }}>
          {/* <button
            onClick={() => {
              createRoom();
              setIsConnected(true);
            }}
            title="Connect"
          >
            <svg width="24" height="24" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
              <g clip-path="url(#clip0_4370_85284)">
                <path
                  d="M12 2C10.34 2 9 3.34 9 5V7H15V5C15 3.34 13.66 2 12 2ZM5 8C3.34 8 2 9.34 2 11V13C2 14.66 3.34 16 5 16H7V8H5ZM19 8H17V16H19C20.66 16 22 14.66 22 13V11C22 9.34 20.66 8 19 8ZM11 18H13V20H11V18Z"
                  fill="#3D3C3C"
                />
                <path d="M7 8L17 8" stroke="#3D3C3C" stroke-width="2" stroke-linecap="round" />
                <path d="M12 16L12 20" stroke="#3D3C3C" stroke-width="2" stroke-linecap="round" />
              </g>
              <defs>
                <clipPath id="clip0_4370_85284">
                  <rect width="24" height="24" fill="white" />
                </clipPath>
              </defs>
            </svg>
          </button> */}
          <svg xmlns="http://www.w3.org/2000/svg" width="24px" height="24px" viewBox="0 0 100 100" preserveAspectRatio="xMidYMid">
            <title>Loading...</title>
            <circle cx="50" cy="50" fill="none" stroke="#1d3f72" strokeWidth="10" r="35" strokeDasharray="164.93361431346415 56.97787143782138">
              <animateTransform attributeName="transform" type="rotate" repeatCount="indefinite" dur="1s" values="0 50 50;360 50 50" keyTimes="0;1" />
            </circle>
          </svg>
        </div>
      )}
      {isConnected && webSocketStatus === "Connected" && !isRecording && (
        <div style={{ textAlign: "right" }}>
          <button onClick={startRecording} title="Start Recording">
            <svg width="24" height="24" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
              <g clip-path="url(#clip0_4370_85283)">
                <path
                  d="M12 13C13.66 13 14.99 11.66 14.99 10L15 4C15 2.34 13.66 1 12 1C10.34 1 9 2.34 9 4V10C9 11.66 10.34 13 12 13ZM19 10H17.3C17.3 13 14.76 15.1 12 15.1C9.24 15.1 6.7 13 6.7 10H5C5 13.41 7.72 16.23 11 16.72V20H13V16.72C16.28 16.23 19 13.41 19 10Z"
                  fill="#3D3C3C"
                />
                <path d="M19 5L5 19" stroke="#3D3C3C" stroke-width="2" stroke-linecap="round" />
                <path
                  d="M7 24H9V22H7V24ZM11 24H13V22H11V24ZM15 24H17V22H15V24ZM12 20V16.72C8.72 16.23 6 13.41 6 10H7.7C7.7 13 10.24 15.1 12 15.1C13.76 15.1 16.3 13 16.3 10H18C18 13.41 15.28 16.23 12 16.72V20H12Z"
                  fill="#3D3C3C"
                />
              </g>
              <defs>
                <clipPath id="clip0_4370_85283">
                  <rect width="24" height="24" fill="white" />
                </clipPath>
              </defs>
            </svg>
          </button>
        </div>
      )}
      {isConnected && isRecording && (
        <div style={{ textAlign: "right" }}>
          <button onClick={stopRecording} title="Stop Recording">
            <svg width="24" height="24" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
              <g clip-path="url(#clip0_4370_85283)">
                <path
                  d="M7 24H9V22H7V24ZM12 13C13.66 13 14.99 11.66 14.99 10L15 4C15 2.34 13.66 1 12 1C10.34 1 9 2.34 9 4V10C9 11.66 10.34 13 12 13ZM11 24H13V22H11V24ZM15 24H17V22H15V24ZM19 10H17.3C17.3 13 14.76 15.1 12 15.1C9.24 15.1 6.7 13 6.7 10H5C5 13.41 7.72 16.23 11 16.72V20H13V16.72C16.28 16.23 19 13.41 19 10Z"
                  fill="#3D3C3C"
                />
              </g>
              <defs>
                <clipPath id="clip0_4370_85283">
                  <rect width="24" height="24" fill="white" />
                </clipPath>
              </defs>
            </svg>
          </button>
        </div>
      )}
    </div>
  );
};

export default TranscriptComponent;
