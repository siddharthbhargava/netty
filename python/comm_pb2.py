# Generated by the protocol buffer compiler.  DO NOT EDIT!

from google.protobuf import descriptor
from google.protobuf import message
from google.protobuf import reflection
from google.protobuf import descriptor_pb2
# @@protoc_insertion_point(imports)



DESCRIPTOR = descriptor.FileDescriptor(
  name='comm.proto',
  package='',
  serialized_pb='\n\ncomm.proto\":\n\x07Request\x12\x17\n\x06header\x18\x01 \x02(\x0b\x32\x07.Header\x12\x16\n\x04\x62ody\x18\x02 \x02(\x0b\x32\x08.Payload\"\xea\x01\n\x06Header\x12#\n\nrouting_id\x18\x01 \x02(\x0e\x32\x0f.Header.Routing\x12\x12\n\noriginator\x18\x02 \x02(\x05\x12\x0b\n\x03tag\x18\x03 \x01(\t\x12\x0c\n\x04time\x18\x04 \x01(\x03\x12!\n\x0bphotoHeader\x18\n \x01(\x0b\x32\x0c.PhotoHeader\x12\x11\n\treply_msg\x18\x06 \x01(\t\x12\x0e\n\x06toNode\x18\x08 \x01(\x05\"F\n\x07Routing\x12\x08\n\x04PING\x10\x02\x12\x0e\n\nNAMESPACES\x10\x03\x12\x08\n\x04JOBS\x10\x04\x12\x0b\n\x07REPORTS\x10\n\x12\n\n\x06MANAGE\x10\x64\".\n\x07Payload\x12#\n\x0cphotoPayload\x18\x04 \x01(\x0b\x32\r.PhotoPayload\"\x83\x02\n\x0bPhotoHeader\x12\x33\n\x0brequestType\x18\x01 \x01(\x0e\x32\x18.PhotoHeader.RequestType:\x04read\x12\x38\n\x0cresponseFlag\x18\x02 \x01(\x0e\x32\x19.PhotoHeader.ResponseFlag:\x07success\x12\x14\n\x0clastModified\x18\x03 \x01(\x03\x12\x15\n\rcontentLength\x18\x04 \x01(\x05\".\n\x0bRequestType\x12\x08\n\x04read\x10\x00\x12\t\n\x05write\x10\x01\x12\n\n\x06\x64\x65lete\x10\x02\"(\n\x0cResponseFlag\x12\x0b\n\x07success\x10\x00\x12\x0b\n\x07\x66\x61ilure\x10\x01\"8\n\x0cPhotoPayload\x12\x0c\n\x04uuid\x18\x01 \x01(\t\x12\x0c\n\x04name\x18\x02 \x01(\t\x12\x0c\n\x04\x64\x61ta\x18\x03 \x01(\x0c\x42\x07\n\x03\x65yeH\x01')



_HEADER_ROUTING = descriptor.EnumDescriptor(
  name='Routing',
  full_name='Header.Routing',
  filename=None,
  file=DESCRIPTOR,
  values=[
    descriptor.EnumValueDescriptor(
      name='PING', index=0, number=2,
      options=None,
      type=None),
    descriptor.EnumValueDescriptor(
      name='NAMESPACES', index=1, number=3,
      options=None,
      type=None),
    descriptor.EnumValueDescriptor(
      name='JOBS', index=2, number=4,
      options=None,
      type=None),
    descriptor.EnumValueDescriptor(
      name='REPORTS', index=3, number=10,
      options=None,
      type=None),
    descriptor.EnumValueDescriptor(
      name='MANAGE', index=4, number=100,
      options=None,
      type=None),
  ],
  containing_type=None,
  options=None,
  serialized_start=239,
  serialized_end=309,
)

_PHOTOHEADER_REQUESTTYPE = descriptor.EnumDescriptor(
  name='RequestType',
  full_name='PhotoHeader.RequestType',
  filename=None,
  file=DESCRIPTOR,
  values=[
    descriptor.EnumValueDescriptor(
      name='read', index=0, number=0,
      options=None,
      type=None),
    descriptor.EnumValueDescriptor(
      name='write', index=1, number=1,
      options=None,
      type=None),
    descriptor.EnumValueDescriptor(
      name='delete', index=2, number=2,
      options=None,
      type=None),
  ],
  containing_type=None,
  options=None,
  serialized_start=531,
  serialized_end=577,
)

_PHOTOHEADER_RESPONSEFLAG = descriptor.EnumDescriptor(
  name='ResponseFlag',
  full_name='PhotoHeader.ResponseFlag',
  filename=None,
  file=DESCRIPTOR,
  values=[
    descriptor.EnumValueDescriptor(
      name='success', index=0, number=0,
      options=None,
      type=None),
    descriptor.EnumValueDescriptor(
      name='failure', index=1, number=1,
      options=None,
      type=None),
  ],
  containing_type=None,
  options=None,
  serialized_start=579,
  serialized_end=619,
)


_REQUEST = descriptor.Descriptor(
  name='Request',
  full_name='Request',
  filename=None,
  file=DESCRIPTOR,
  containing_type=None,
  fields=[
    descriptor.FieldDescriptor(
      name='header', full_name='Request.header', index=0,
      number=1, type=11, cpp_type=10, label=2,
      has_default_value=False, default_value=None,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
    descriptor.FieldDescriptor(
      name='body', full_name='Request.body', index=1,
      number=2, type=11, cpp_type=10, label=2,
      has_default_value=False, default_value=None,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
  ],
  extensions=[
  ],
  nested_types=[],
  enum_types=[
  ],
  options=None,
  is_extendable=False,
  extension_ranges=[],
  serialized_start=14,
  serialized_end=72,
)


_HEADER = descriptor.Descriptor(
  name='Header',
  full_name='Header',
  filename=None,
  file=DESCRIPTOR,
  containing_type=None,
  fields=[
    descriptor.FieldDescriptor(
      name='routing_id', full_name='Header.routing_id', index=0,
      number=1, type=14, cpp_type=8, label=2,
      has_default_value=False, default_value=2,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
    descriptor.FieldDescriptor(
      name='originator', full_name='Header.originator', index=1,
      number=2, type=5, cpp_type=1, label=2,
      has_default_value=False, default_value=0,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
    descriptor.FieldDescriptor(
      name='tag', full_name='Header.tag', index=2,
      number=3, type=9, cpp_type=9, label=1,
      has_default_value=False, default_value=unicode("", "utf-8"),
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
    descriptor.FieldDescriptor(
      name='time', full_name='Header.time', index=3,
      number=4, type=3, cpp_type=2, label=1,
      has_default_value=False, default_value=0,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
    descriptor.FieldDescriptor(
      name='photoHeader', full_name='Header.photoHeader', index=4,
      number=10, type=11, cpp_type=10, label=1,
      has_default_value=False, default_value=None,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
    descriptor.FieldDescriptor(
      name='reply_msg', full_name='Header.reply_msg', index=5,
      number=6, type=9, cpp_type=9, label=1,
      has_default_value=False, default_value=unicode("", "utf-8"),
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
    descriptor.FieldDescriptor(
      name='toNode', full_name='Header.toNode', index=6,
      number=8, type=5, cpp_type=1, label=1,
      has_default_value=False, default_value=0,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
  ],
  extensions=[
  ],
  nested_types=[],
  enum_types=[
    _HEADER_ROUTING,
  ],
  options=None,
  is_extendable=False,
  extension_ranges=[],
  serialized_start=75,
  serialized_end=309,
)


_PAYLOAD = descriptor.Descriptor(
  name='Payload',
  full_name='Payload',
  filename=None,
  file=DESCRIPTOR,
  containing_type=None,
  fields=[
    descriptor.FieldDescriptor(
      name='photoPayload', full_name='Payload.photoPayload', index=0,
      number=4, type=11, cpp_type=10, label=1,
      has_default_value=False, default_value=None,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
  ],
  extensions=[
  ],
  nested_types=[],
  enum_types=[
  ],
  options=None,
  is_extendable=False,
  extension_ranges=[],
  serialized_start=311,
  serialized_end=357,
)


_PHOTOHEADER = descriptor.Descriptor(
  name='PhotoHeader',
  full_name='PhotoHeader',
  filename=None,
  file=DESCRIPTOR,
  containing_type=None,
  fields=[
    descriptor.FieldDescriptor(
      name='requestType', full_name='PhotoHeader.requestType', index=0,
      number=1, type=14, cpp_type=8, label=1,
      has_default_value=True, default_value=0,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
    descriptor.FieldDescriptor(
      name='responseFlag', full_name='PhotoHeader.responseFlag', index=1,
      number=2, type=14, cpp_type=8, label=1,
      has_default_value=True, default_value=0,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
    descriptor.FieldDescriptor(
      name='lastModified', full_name='PhotoHeader.lastModified', index=2,
      number=3, type=3, cpp_type=2, label=1,
      has_default_value=False, default_value=0,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
    descriptor.FieldDescriptor(
      name='contentLength', full_name='PhotoHeader.contentLength', index=3,
      number=4, type=5, cpp_type=1, label=1,
      has_default_value=False, default_value=0,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
  ],
  extensions=[
  ],
  nested_types=[],
  enum_types=[
    _PHOTOHEADER_REQUESTTYPE,
    _PHOTOHEADER_RESPONSEFLAG,
  ],
  options=None,
  is_extendable=False,
  extension_ranges=[],
  serialized_start=360,
  serialized_end=619,
)


_PHOTOPAYLOAD = descriptor.Descriptor(
  name='PhotoPayload',
  full_name='PhotoPayload',
  filename=None,
  file=DESCRIPTOR,
  containing_type=None,
  fields=[
    descriptor.FieldDescriptor(
      name='uuid', full_name='PhotoPayload.uuid', index=0,
      number=1, type=9, cpp_type=9, label=1,
      has_default_value=False, default_value=unicode("", "utf-8"),
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
    descriptor.FieldDescriptor(
      name='name', full_name='PhotoPayload.name', index=1,
      number=2, type=9, cpp_type=9, label=1,
      has_default_value=False, default_value=unicode("", "utf-8"),
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
    descriptor.FieldDescriptor(
      name='data', full_name='PhotoPayload.data', index=2,
      number=3, type=12, cpp_type=9, label=1,
      has_default_value=False, default_value="",
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
  ],
  extensions=[
  ],
  nested_types=[],
  enum_types=[
  ],
  options=None,
  is_extendable=False,
  extension_ranges=[],
  serialized_start=621,
  serialized_end=677,
)

_REQUEST.fields_by_name['header'].message_type = _HEADER
_REQUEST.fields_by_name['body'].message_type = _PAYLOAD
_HEADER.fields_by_name['routing_id'].enum_type = _HEADER_ROUTING
_HEADER.fields_by_name['photoHeader'].message_type = _PHOTOHEADER
_HEADER_ROUTING.containing_type = _HEADER;
_PAYLOAD.fields_by_name['photoPayload'].message_type = _PHOTOPAYLOAD
_PHOTOHEADER.fields_by_name['requestType'].enum_type = _PHOTOHEADER_REQUESTTYPE
_PHOTOHEADER.fields_by_name['responseFlag'].enum_type = _PHOTOHEADER_RESPONSEFLAG
_PHOTOHEADER_REQUESTTYPE.containing_type = _PHOTOHEADER;
_PHOTOHEADER_RESPONSEFLAG.containing_type = _PHOTOHEADER;
DESCRIPTOR.message_types_by_name['Request'] = _REQUEST
DESCRIPTOR.message_types_by_name['Header'] = _HEADER
DESCRIPTOR.message_types_by_name['Payload'] = _PAYLOAD
DESCRIPTOR.message_types_by_name['PhotoHeader'] = _PHOTOHEADER
DESCRIPTOR.message_types_by_name['PhotoPayload'] = _PHOTOPAYLOAD

class Request(message.Message):
  __metaclass__ = reflection.GeneratedProtocolMessageType
  DESCRIPTOR = _REQUEST
  
  # @@protoc_insertion_point(class_scope:Request)

class Header(message.Message):
  __metaclass__ = reflection.GeneratedProtocolMessageType
  DESCRIPTOR = _HEADER
  
  # @@protoc_insertion_point(class_scope:Header)

class Payload(message.Message):
  __metaclass__ = reflection.GeneratedProtocolMessageType
  DESCRIPTOR = _PAYLOAD
  
  # @@protoc_insertion_point(class_scope:Payload)

class PhotoHeader(message.Message):
  __metaclass__ = reflection.GeneratedProtocolMessageType
  DESCRIPTOR = _PHOTOHEADER
  
  # @@protoc_insertion_point(class_scope:PhotoHeader)

class PhotoPayload(message.Message):
  __metaclass__ = reflection.GeneratedProtocolMessageType
  DESCRIPTOR = _PHOTOPAYLOAD
  
  # @@protoc_insertion_point(class_scope:PhotoPayload)

# @@protoc_insertion_point(module_scope)
