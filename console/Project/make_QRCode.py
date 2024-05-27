import uuid
import os
import qrcode

class UUID2QRCode:
    def __init__(self) -> None:
        pass

    def generate_uuid(self):
        self.uuid = uuid.uuid4()

    def generate_qrcode(self):
        self.qrcode = qrcode.QRCode(
            version=1,
            error_correction=qrcode.constants.ERROR_CORRECT_L,
            box_size=10,
            border=4,
        )
        self.qrcode.add_data(self.uuid)
        self.qrcode.make(fit=True)

        img = self.qrcode.make_image(fill_color="black", back_color="white")
        img.save(os.path.join("data_base", self.uuid))
