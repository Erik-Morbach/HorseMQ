import networkUtils as nu
import struct
import numpy as np
import random
import time
from queue import Queue
import threading as th


amp = 1
freq = 2  # hz
duration = 10  # s
sampleRate = 44100

producerName = "Prod"

producerId = nu.createProducerId(producerName)[1].decode('utf-8')

queueId = nu.createQueueId(["con1"]).decode('utf-8')

print(f"producer = {producerName}, {producerId}")
print(f"queue = {queueId}")

s = nu.producerBeginSendData(producerId, queueId)


inputQueue = Queue()


def populateQueue():
    t0 = time.perf_counter()
    while 1:
        time.sleep(0.2)
        t1 = time.perf_counter()
        diff = t1 - t0
        tt0 = time.perf_counter()
        inputQueue.put(np.sin(2*np.pi*freq*np.linspace(t0, t1, int(diff*sampleRate))))
        t0 = tt0


th.Thread(target=populateQueue).start()

data = []
while 1:
    if len(data) == 0:
        data = inputQueue.get()
    currentBlock = random.randint(1, min(512, len(data)))
    sendData = struct.pack(">i", currentBlock)
    #print(f"Sending {currentBlock} values")
    sendData += struct.pack(">"+str(currentBlock)+"d", *data[:currentBlock])
    #print(f"Sended {len(sendData)}")
    s.send(sendData)
    data = data[currentBlock:]
