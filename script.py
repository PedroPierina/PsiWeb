

import tensorflow as tf

imported = tf.keras.models.load_model('C:/Users/Pedro.DESKTOP-J36MF19/Desktop/saved_model')

# y_test = "A psicologia humanista é um ramo da psicologia em geral, e da psicoterapia, considerada como a terceira força, ao lado da psicanálise e da psicologia comportamental. A psicologia humanista surgiu como uma reação ao determinismo dominante nas outras práticas psicoterapêuticas, ensinando que o ser humano possui em si uma força de autorrealização, que conduz o indivíduo ao desenvolvimento de uma personalidade criativa e saudável."
# prediction = tf.expand_dims(y_test, axis=0)   # the shape would be (1, 224, 224, 3)

# print(imported.predict_classes(prediction)[0])
