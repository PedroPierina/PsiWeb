import sys
import os
import warnings
warnings.filterwarnings("ignore")
os.environ['TF_CPP_MIN_LOG_LEVEL'] = '3'
import tensorflow as tf
import tensorflow_text as text
import numpy as np

model = tf.keras.models.load_model('/home/pedro/Desktop/saved_model/my_model')

# input = "A psicologia humanista é um ramo da psicologia em geral, e da psicoterapia, considerada como a terceira força, ao lado da psicanálise e da psicologia comportamental. A psicologia humanista surgiu como uma reação ao determinismo dominante nas outras práticas psicoterapêuticas, ensinando que o ser humano possui em si uma força de autorrealização, que conduz o indivíduo ao desenvolvimento de uma personalidade criativa e saudável."
input= sys.argv[1]
prediction = tf.expand_dims(input, axis=0)   # the shape would be (1, 224, 224, 3)

print(np.argmax(model.predict(prediction)[0], axis=-1))
